/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModParticleTypes;
import moriyashiine.enchancement.common.payload.StartSlidingC2SPayload;
import moriyashiine.enchancement.common.payload.StopSlidingC2SPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2d;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class SlideComponent implements CommonTickingComponent {
	private static final EntityAttributeModifier SAFE_FALL_DISTANCE_MODIFIER = new EntityAttributeModifier(Enchancement.id("slide_safe_fall_distance"), 6, EntityAttributeModifier.Operation.ADD_VALUE);
	private static final EntityAttributeModifier STEP_HEIGHT_MODIFIER = new EntityAttributeModifier(Enchancement.id("slide_step_height"), 1, EntityAttributeModifier.Operation.ADD_VALUE);

	private static final int MAX_SLIDING_TICKS = 40, MAX_WATER_SKIP_TICKS = 30;

	private final PlayerEntity obj;
	private SlideVelocity velocity = SlideVelocity.ZERO, adjustedVelocity = SlideVelocity.ZERO;
	private float cachedYaw = 0;
	private int slidingTicks = 0;

	private float strength = 0;
	private boolean hasSlide = false;

	private int crawlTimer = 0, waterSkipTicks = 0;

	public SlideComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		velocity = readView.read("Velocity", SlideVelocity.CODEC).orElse(SlideVelocity.ZERO);
		adjustedVelocity = readView.read("AdjustedVelocity", SlideVelocity.CODEC).orElse(SlideVelocity.ZERO);
		cachedYaw = readView.getFloat("CachedYaw", 0);
		slidingTicks = readView.getInt("SlidingTicks", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.put("Velocity", SlideVelocity.CODEC, velocity);
		writeView.put("AdjustedVelocity", SlideVelocity.CODEC, adjustedVelocity);
		writeView.putFloat("CachedYaw", cachedYaw);
		writeView.putInt("SlidingTicks", slidingTicks);
	}

	@Override
	public void tick() {
		boolean hasFluidWalking = EnchancementUtil.hasAnyEnchantmentsWith(obj, ModEnchantmentEffectComponentTypes.FLUID_WALKING);
		strength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.SLIDE, obj, 0);
		hasSlide = strength > 0;
		if (crawlTimer > 0) {
			crawlTimer--;
		}
		if (hasSlide) {
			if (waterSkipTicks >= MAX_WATER_SKIP_TICKS || obj.isSneaking() || (obj.isTouchingWater() && !hasFluidWalking)) {
				stopSliding();
			}
			if (isSliding()) {
				if (updateCrawl()) {
					crawlTimer = 3;
				}
				obj.spawnSprintingParticles();
				double dX = adjustedVelocity.x(), dZ = adjustedVelocity.z();
				if (!obj.isOnGround()) {
					dX *= 0.2;
					dZ *= 0.2;
				}
				float multiplier = MultiplyMovementSpeedEvent.getMovementMultiplier(obj);
				multiplier *= 1 - (waterSkipTicks / (MAX_WATER_SKIP_TICKS * 2F));
				obj.addVelocity(dX * multiplier, 0, dZ * multiplier);
				if (obj.isTouchingWater() && hasFluidWalking) {
					obj.setVelocity(obj.getVelocity().getX(), strength, obj.getVelocity().getZ());
					waterSkipTicks++;
				}
				if (slidingTicks < MAX_SLIDING_TICKS) {
					slidingTicks++;
				}
			} else {
				if (slidingTicks > 0) {
					slidingTicks = Math.max(0, slidingTicks - 4);
				}
				waterSkipTicks = 0;
			}
		} else {
			stopSliding();
			slidingTicks = 0;
			waterSkipTicks = 0;
		}
	}

	@Override
	public void serverTick() {
		tick();
		EntityAttributeInstance safeFallDistanceAttribute = obj.getAttributeInstance(EntityAttributes.SAFE_FALL_DISTANCE);
		EntityAttributeInstance stepHeightAttribute = obj.getAttributeInstance(EntityAttributes.STEP_HEIGHT);
		if (hasSlide && isSliding()) {
			if (!safeFallDistanceAttribute.hasModifier(SAFE_FALL_DISTANCE_MODIFIER.id())) {
				safeFallDistanceAttribute.addPersistentModifier(SAFE_FALL_DISTANCE_MODIFIER);
			}
			if (!stepHeightAttribute.hasModifier(STEP_HEIGHT_MODIFIER.id())) {
				stepHeightAttribute.addPersistentModifier(STEP_HEIGHT_MODIFIER);
			}
		} else {
			if (safeFallDistanceAttribute.hasModifier(SAFE_FALL_DISTANCE_MODIFIER.id())) {
				safeFallDistanceAttribute.removeModifier(SAFE_FALL_DISTANCE_MODIFIER);
			}
			if (stepHeightAttribute.hasModifier(STEP_HEIGHT_MODIFIER.id())) {
				stepHeightAttribute.removeModifier(STEP_HEIGHT_MODIFIER);
			}
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasSlide) {
			if (!obj.isSpectator() && SLibClientUtils.isHost(obj)) {
				GameOptions options = MinecraftClient.getInstance().options;
				if (EnchancementClient.SLIDE_KEYBINDING.isPressed() && !obj.isSneaking() && !obj.jumping) {
					if (canSlide()) {
						velocity = getVelocityFromInput(options);
						adjustedVelocity = velocity.rotateY((float) Math.toRadians(-(obj.getYaw() + 90)));
						cachedYaw = obj.getYaw();
						StartSlidingC2SPayload.send(velocity, adjustedVelocity, cachedYaw);
					}
				} else if (velocity != SlideVelocity.ZERO) {
					stopSliding();
					StopSlidingC2SPayload.send();
				}
			}
			if (isSliding() && SLibClientUtils.shouldAddParticles(obj)) {
				Vector2d vec = new Vector2d(adjustedVelocity.x(), adjustedVelocity.z());
				vec.normalize();
				vec.mul(obj.getWidth() / 2);
				obj.getWorld().addParticleClient(ModParticleTypes.VELOCITY_LINE, obj.getX() - vec.y(), obj.getY() + obj.getHeight() / 2 + MathHelper.nextFloat(obj.getRandom(), -obj.getHeight() / 3, obj.getHeight() / 3), obj.getZ() + vec.x(), adjustedVelocity.x(), 0, adjustedVelocity.z());
				obj.getWorld().addParticleClient(ModParticleTypes.VELOCITY_LINE, obj.getX() + vec.y(), obj.getY() + obj.getHeight() / 2 + MathHelper.nextFloat(obj.getRandom(), -obj.getHeight() / 3, obj.getHeight() / 3), obj.getZ() - vec.x(), adjustedVelocity.x(), 0, adjustedVelocity.z());
			}
		}
	}

	public SlideVelocity getVelocity() {
		return velocity;
	}

	public float getCachedYaw() {
		return cachedYaw;
	}

	public void startSliding(SlideVelocity velocity, SlideVelocity adjustedVelocity, float cachedYaw) {
		this.velocity = velocity;
		this.adjustedVelocity = adjustedVelocity;
		this.cachedYaw = cachedYaw;
	}

	public void stopSliding() {
		startSliding(SlideVelocity.ZERO, SlideVelocity.ZERO, 0);
	}

	public boolean isSliding() {
		return !velocity.equals(SlideVelocity.ZERO);
	}

	public float getJumpBonus() {
		return MathHelper.lerp(slidingTicks / (float) MAX_SLIDING_TICKS, 1F, 3F);
	}

	public boolean hasSlide() {
		return hasSlide;
	}

	public boolean shouldCrawl() {
		return crawlTimer > 0;
	}

	public boolean canSlide() {
		return !isSliding() && obj.isOnGround() && SLibUtils.isGroundedOrAirborne(obj);
	}

	private boolean hitsBlock(BlockPos pos) {
		return !obj.getWorld().getBlockState(pos).getCollisionShape(obj.getWorld(), pos).isEmpty();
	}

	private boolean updateCrawl() {
		int height = MathHelper.floor(obj.getHeight());
		if (height > 0) {
			Vec3d frontPos = obj.getPos().add(0, height, 0).add(obj.getRotationVector(0, cachedYaw));
			BlockPos.Mutable pos = new BlockPos.Mutable(frontPos.getX(), frontPos.getY(), frontPos.getZ());
			int y = pos.getY();
			boolean hitsBelow = hitsBlock(pos.setY(y - 1));
			if (hitsBlock(pos.setY(y))) {
				return !hitsBelow;
			} else if (hitsBelow) {
				return hitsBlock(pos.setY(y + 1)) || hitsBlock(obj.getBlockPos().up(height + 1));
			}
		}
		return false;
	}

	private SlideVelocity getVelocityFromInput(GameOptions options) {
		boolean any = false, forward = false, sideways = false;
		int x = 0, z = 0;
		if (options.forwardKey.isPressed()) {
			any = true;
			forward = true;
			x = 1;
		}
		if (options.backKey.isPressed()) {
			any = true;
			forward = true;
			x = -1;
		}
		if (options.leftKey.isPressed()) {
			any = true;
			sideways = true;
			z = -1;
		}
		if (options.rightKey.isPressed()) {
			any = true;
			sideways = true;
			z = 1;
		}
		return new SlideVelocity(any ? x : 1, z).multiply(forward && sideways ? 2 / 3F : 1).multiply(strength);
	}

	public record SlideVelocity(float x, float z) {
		public static final Codec<SlideVelocity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Codec.FLOAT.fieldOf("x").forGetter(SlideVelocity::x),
						Codec.FLOAT.fieldOf("z").forGetter(SlideVelocity::z))
				.apply(instance, SlideVelocity::new));
		public static final PacketCodec<PacketByteBuf, SlideVelocity> PACKET_CODEC = PacketCodec.tuple(
				PacketCodecs.FLOAT, SlideVelocity::x,
				PacketCodecs.FLOAT, SlideVelocity::z,
				SlideVelocity::new
		);

		public static final SlideVelocity ZERO = new SlideVelocity(0, 0);

		SlideVelocity multiply(float value) {
			return new SlideVelocity(x() * value, z() * value);
		}

		SlideVelocity rotateY(float angle) {
			float cos = MathHelper.cos(angle);
			float sin = MathHelper.sin(angle);
			float nX = x() * cos + z() * sin;
			float nZ = z() * cos - x() * sin;
			return new SlideVelocity(nX, nZ);
		}
	}
}
