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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2d;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class SlideComponent implements CommonTickingComponent {
	private static final AttributeModifier SAFE_FALL_DISTANCE_MODIFIER = new AttributeModifier(Enchancement.id("slide_safe_fall_distance"), 6, AttributeModifier.Operation.ADD_VALUE);
	private static final AttributeModifier STEP_HEIGHT_MODIFIER = new AttributeModifier(Enchancement.id("slide_step_height"), 1, AttributeModifier.Operation.ADD_VALUE);

	private static final int MAX_SLIDING_TICKS = 10, MAX_WATER_SKIP_TICKS = 30;

	private final Player obj;
	private SlideDeltaMovement delta = SlideDeltaMovement.ZERO, adjustedDelta = SlideDeltaMovement.ZERO;
	private float cachedYRot = 0;
	private int slidingTicks = 0;

	private float strength = 0;
	private boolean hasSlide = false;

	private int crawlTimer = 0, waterSkipTicks = 0;

	public SlideComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		delta = input.read("Delta", SlideDeltaMovement.CODEC).orElse(SlideDeltaMovement.ZERO);
		adjustedDelta = input.read("AdjustedDelta", SlideDeltaMovement.CODEC).orElse(SlideDeltaMovement.ZERO);
		cachedYRot = input.getFloatOr("CachedYRot", 0);
		slidingTicks = input.getIntOr("SlidingTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.store("Delta", SlideDeltaMovement.CODEC, delta);
		output.store("AdjustedDelta", SlideDeltaMovement.CODEC, adjustedDelta);
		output.putFloat("CachedYRot", cachedYRot);
		output.putInt("SlidingTicks", slidingTicks);
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
			if (waterSkipTicks >= MAX_WATER_SKIP_TICKS || obj.isShiftKeyDown() || obj.isSpectator() || (obj.isInWater() && !hasFluidWalking)) {
				stopSliding();
			}
			if (isSliding()) {
				if (updateCrawl()) {
					crawlTimer = 3;
				}
				obj.spawnSprintParticle();
				double dX = adjustedDelta.x(), dZ = adjustedDelta.z();
				if (!obj.onGround()) {
					dX *= 0.2;
					dZ *= 0.2;
				}
				float multiplier = MultiplyMovementSpeedEvent.getMovementMultiplier(obj);
				multiplier *= 1 - (waterSkipTicks / (MAX_WATER_SKIP_TICKS * 2F));
				obj.push(dX * multiplier, 0, dZ * multiplier);
				if (obj.isInWater() && hasFluidWalking) {
					obj.setDeltaMovement(obj.getDeltaMovement().x(), strength, obj.getDeltaMovement().z());
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
		boolean sliding = hasSlide && isSliding();
		SLibUtils.conditionallyApplyAttributeModifier(obj, Attributes.SAFE_FALL_DISTANCE, SAFE_FALL_DISTANCE_MODIFIER, sliding);
		SLibUtils.conditionallyApplyAttributeModifier(obj, Attributes.STEP_HEIGHT, STEP_HEIGHT_MODIFIER, sliding);
	}

	@Override
	public void clientTick() {
		tick();
		if (hasSlide) {
			if (!obj.isSpectator() && SLibClientUtils.isHost(obj)) {
				if (EnchancementClient.SLIDE_KEYMAPPING.isDown() && !obj.isShiftKeyDown() && !obj.jumping) {
					if (canSlide()) {
						delta = getDeltaMovementFromInput();
						adjustedDelta = delta.rotateY((float) Math.toRadians(-(obj.getYRot() + 90)));
						cachedYRot = obj.getYRot();
						StartSlidingC2SPayload.send(delta, adjustedDelta, cachedYRot);
					}
				} else if (delta != SlideDeltaMovement.ZERO) {
					stopSliding();
					StopSlidingC2SPayload.send();
				}
			}
			if (isSliding()) {
				Vector2d vec = new Vector2d(adjustedDelta.x(), adjustedDelta.z());
				vec.normalize();
				vec.mul(obj.getBbWidth() / 2);
				obj.level().addParticle(ModParticleTypes.VELOCITY_LINE, obj.getX() - vec.y(), obj.getY() + obj.getBbHeight() / 2 + Mth.nextFloat(obj.getRandom(), -obj.getBbHeight() / 3, obj.getBbHeight() / 3), obj.getZ() + vec.x(), adjustedDelta.x(), 0, adjustedDelta.z());
				obj.level().addParticle(ModParticleTypes.VELOCITY_LINE, obj.getX() + vec.y(), obj.getY() + obj.getBbHeight() / 2 + Mth.nextFloat(obj.getRandom(), -obj.getBbHeight() / 3, obj.getBbHeight() / 3), obj.getZ() - vec.x(), adjustedDelta.x(), 0, adjustedDelta.z());
			}
		}
	}

	public SlideDeltaMovement getDelta() {
		return delta;
	}

	public float getCachedYRot() {
		return cachedYRot;
	}

	public void startSliding(SlideDeltaMovement delta, SlideDeltaMovement adjustedDelta, float cachedYRot) {
		this.delta = delta;
		this.adjustedDelta = adjustedDelta;
		this.cachedYRot = cachedYRot;
	}

	public void stopSliding() {
		startSliding(SlideDeltaMovement.ZERO, SlideDeltaMovement.ZERO, 0);
	}

	public boolean isSliding() {
		return !delta.equals(SlideDeltaMovement.ZERO);
	}

	public float getJumpBonus() {
		return Mth.lerp(slidingTicks / (float) MAX_SLIDING_TICKS, 1, 3);
	}

	public boolean hasSlide() {
		return hasSlide;
	}

	public boolean shouldCrawl() {
		return crawlTimer > 0;
	}

	public boolean canSlide() {
		return !isSliding() && obj.onGround() && SLibUtils.isGroundedOrAirborne(obj);
	}

	private boolean hitsBlock(BlockPos pos) {
		return !obj.level().getBlockState(pos).getCollisionShape(obj.level(), pos).isEmpty();
	}

	private boolean updateCrawl() {
		int height = Mth.floor(obj.getBbHeight());
		if (height > 0) {
			Vec3 frontPos = obj.position().add(0, height, 0).add(obj.calculateViewVector(0, cachedYRot));
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(frontPos.x(), frontPos.y(), frontPos.z());
			int y = pos.getY();
			boolean hitsBelow = hitsBlock(pos.setY(y - 1));
			if (hitsBlock(pos.setY(y))) {
				return !hitsBelow;
			} else if (hitsBelow) {
				return hitsBlock(pos.setY(y + 1)) || hitsBlock(obj.blockPosition().above(height + 1));
			}
		}
		return false;
	}

	@Environment(EnvType.CLIENT)
	private SlideDeltaMovement getDeltaMovementFromInput() {
		Options options = Minecraft.getInstance().options;
		boolean any = false, forward = false, sideways = false;
		int x = 0, z = 0;
		if (options.keyUp.isDown()) {
			any = true;
			forward = true;
			x = 1;
		}
		if (options.keyDown.isDown()) {
			any = true;
			forward = true;
			x = -1;
		}
		if (options.keyLeft.isDown()) {
			any = true;
			sideways = true;
			z = -1;
		}
		if (options.keyRight.isDown()) {
			any = true;
			sideways = true;
			z = 1;
		}
		return new SlideDeltaMovement(any ? x : 1, z).multiply(forward && sideways ? 2 / 3F : 1).multiply(strength);
	}

	public record SlideDeltaMovement(float x, float z) {
		public static final Codec<SlideDeltaMovement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Codec.FLOAT.fieldOf("x").forGetter(SlideDeltaMovement::x),
						Codec.FLOAT.fieldOf("z").forGetter(SlideDeltaMovement::z))
				.apply(instance, SlideDeltaMovement::new));
		public static final StreamCodec<FriendlyByteBuf, SlideDeltaMovement> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.FLOAT, SlideDeltaMovement::x,
				ByteBufCodecs.FLOAT, SlideDeltaMovement::z,
				SlideDeltaMovement::new
		);

		public static final SlideDeltaMovement ZERO = new SlideDeltaMovement(0, 0);

		SlideDeltaMovement multiply(float value) {
			return new SlideDeltaMovement(x() * value, z() * value);
		}

		SlideDeltaMovement rotateY(float angle) {
			float cos = Mth.cos(angle);
			float sin = Mth.sin(angle);
			float nX = x() * cos + z() * sin;
			float nZ = z() * cos - x() * sin;
			return new SlideDeltaMovement(nX, nZ);
		}
	}
}
