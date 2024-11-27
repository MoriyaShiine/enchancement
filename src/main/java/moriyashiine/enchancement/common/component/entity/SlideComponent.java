/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.payload.StartSlidingPayload;
import moriyashiine.enchancement.common.payload.StopSlidingPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class SlideComponent implements CommonTickingComponent {
	private static final EntityAttributeModifier SAFE_FALL_DISTANCE_MODIFIER = new EntityAttributeModifier(Enchancement.id("slide_safe_fall_distance"), 6, EntityAttributeModifier.Operation.ADD_VALUE);
	private static final EntityAttributeModifier STEP_HEIGHT_MODIFIER = new EntityAttributeModifier(Enchancement.id("slide_step_height"), 1, EntityAttributeModifier.Operation.ADD_VALUE);

	private static final int MAX_SLIDING_TICKS = 40;

	private final PlayerEntity obj;
	private SlideVelocity velocity = SlideVelocity.ZERO, adjustedVelocity = SlideVelocity.ZERO;
	private float cachedYaw = 0;
	private int slidingTicks = 0;

	private float strength = 0;
	private boolean hasSlide = false;

	private int crawlTimer = 0;

	public SlideComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		velocity = SlideVelocity.deserialize(tag.getCompound("Velocity"));
		adjustedVelocity = SlideVelocity.deserialize(tag.getCompound("AdjustedVelocity"));
		cachedYaw = tag.getFloat("CachedYaw");
		slidingTicks = tag.getInt("SlidingTicks");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.put("Velocity", velocity.serialize());
		tag.put("AdjustedVelocity", adjustedVelocity.serialize());
		tag.putFloat("CachedYaw", cachedYaw);
		tag.putInt("SlidingTicks", slidingTicks);
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
			if (obj.isSneaking() || (obj.isTouchingWater() && !hasFluidWalking)) {
				stopSliding();
			}
			if (isSliding()) {
				if (updateCrawl()) {
					crawlTimer = 3;
				}
				obj.spawnSprintingParticles();
				obj.getWorld().emitGameEvent(GameEvent.STEP, obj.getPos(), GameEvent.Emitter.of(obj.getSteppingBlockState()));
				double dX = adjustedVelocity.x(), dZ = adjustedVelocity.z();
				if (!obj.isOnGround()) {
					dX *= 0.2;
					dZ *= 0.2;
				}
				float multiplier = MultiplyMovementSpeedEvent.getMovementMultiplier(obj);
				obj.addVelocity(dX * multiplier, 0, dZ * multiplier);
				if (obj.isTouchingWater() && hasFluidWalking) {
					obj.setVelocity(obj.getVelocity().getX(), strength, obj.getVelocity().getZ());
				}
				if (slidingTicks < MAX_SLIDING_TICKS) {
					slidingTicks++;
				}
			} else if (slidingTicks > 0) {
				slidingTicks = Math.max(0, slidingTicks - 4);
			}
		} else {
			stopSliding();
			slidingTicks = 0;
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
		if (hasSlide && !obj.isSpectator() && obj == MinecraftClient.getInstance().player) {
			GameOptions options = MinecraftClient.getInstance().options;
			if (EnchancementClient.SLIDE_KEYBINDING.isPressed() && !obj.isSneaking() && !obj.jumping) {
				if (canSlide()) {
					velocity = getVelocityFromInput(options);
					adjustedVelocity = velocity.rotateY((float) Math.toRadians(-(obj.getYaw() + 90)));
					cachedYaw = obj.getYaw();
					StartSlidingPayload.send(velocity, adjustedVelocity, cachedYaw);
				}
			} else if (velocity != SlideVelocity.ZERO) {
				stopSliding();
				StopSlidingPayload.send();
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
		return !isSliding() && obj.isOnGround() && EnchancementUtil.isGroundedOrAirborne(obj);
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
		public static final SlideVelocity ZERO = new SlideVelocity(0, 0);

		private static SlideVelocity deserialize(NbtCompound nbt) {
			return new SlideVelocity(nbt.getFloat("VelocityX"), nbt.getFloat("VelocityZ"));
		}

		private NbtCompound serialize() {
			NbtCompound velocity = new NbtCompound();
			velocity.putFloat("VelocityX", x());
			velocity.putFloat("VelocityZ", z());
			return velocity;
		}

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
