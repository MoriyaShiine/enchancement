/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.packet.SlideSlamPacket;
import moriyashiine.enchancement.common.packet.SlideVelocityPacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModScaleTypes;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.slide.EntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import virtuoel.pehkui.api.ScaleData;

import java.util.UUID;

public class SlideComponent implements CommonTickingComponent {
	public static final int DEFAULT_JUMP_BOOST_RESET_TICKS = 5, DEFAULT_SLAM_COOLDOWN = 7;

	private static final EntityAttributeModifier STEP_HEIGHT_INCREASE = new EntityAttributeModifier(UUID.fromString("f95ce6ed-ecf3-433b-a7f0-a9c6092b0cf7"), "Enchantment modifier", 1, EntityAttributeModifier.Operation.ADDITION);

	private final PlayerEntity obj;
	private Vec3d velocity = Vec3d.ZERO;
	private boolean shouldSlam = false;
	private int jumpBoostResetTicks = DEFAULT_JUMP_BOOST_RESET_TICKS, slamCooldown = DEFAULT_SLAM_COOLDOWN, ticksLeftToJump = 0, timesJumped = 0, ticksSliding = 0;

	private boolean hasSlide = false;

	private boolean disallowSlide = false, wasSneaking = false;

	public SlideComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(@NotNull NbtCompound tag) {
		velocity = new Vec3d(tag.getDouble("VelocityX"), tag.getDouble("VelocityY"), tag.getDouble("VelocityZ"));
		shouldSlam = tag.getBoolean("ShouldSlam");
		jumpBoostResetTicks = tag.getInt("JumpBoostResetTicks");
		slamCooldown = tag.getInt("SlamCooldown");
		ticksLeftToJump = tag.getInt("TicksLeftToJump");
		timesJumped = tag.getInt("TimesJumped");
		ticksSliding = tag.getInt("TicksSliding");
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		tag.putDouble("VelocityX", velocity.getX());
		tag.putDouble("VelocityY", velocity.getY());
		tag.putDouble("VelocityZ", velocity.getZ());
		tag.putBoolean("ShouldSlam", shouldSlam);
		tag.putInt("JumpBoostResetTicks", jumpBoostResetTicks);
		tag.putInt("SlamCooldown", slamCooldown);
		tag.putInt("TicksLeftToJump", ticksLeftToJump);
		tag.putInt("TimesJumped", timesJumped);
		tag.putInt("TicksSliding", ticksSliding);
	}

	@Override
	public void tick() {
		hasSlide = EnchancementUtil.hasEnchantment(ModEnchantments.SLIDE, obj);
		if (hasSlide) {
			if (obj.isSneaking() || obj.isTouchingWater()) {
				velocity = Vec3d.ZERO;
			}
			if (jumpBoostResetTicks > 0 && timesJumped > 0 && obj.isOnGround() && --jumpBoostResetTicks == 0) {
				timesJumped = 0;
			}
			if (slamCooldown > 0) {
				slamCooldown--;
			}
			if (ticksLeftToJump > 0) {
				ticksLeftToJump--;
			}
			if (isSliding()) {
				((EntityAccessor) obj).enchancement$spawnSprintingParticles();
				obj.getWorld().emitGameEvent(GameEvent.STEP, obj.getPos(), GameEvent.Emitter.of(obj.getSteppingBlockState()));
				if (obj.isOnGround()) {
					obj.setVelocity(velocity.getX(), obj.getVelocity().getY(), velocity.getZ());
				} else {
					obj.setVelocity(velocity.getX() * 0.8, obj.getVelocity().getY(), velocity.getZ() * 0.8);
				}
				obj.velocityDirty = true;
				obj.velocityModified = true;
				if (ticksSliding < 60) {
					ticksSliding++;
				}
			} else if (ticksSliding > 0) {
				ticksSliding = Math.max(0, ticksSliding - 4);
			}
		} else {
			velocity = Vec3d.ZERO;
			shouldSlam = false;
			jumpBoostResetTicks = DEFAULT_JUMP_BOOST_RESET_TICKS;
			slamCooldown = DEFAULT_SLAM_COOLDOWN;
			ticksLeftToJump = 0;
			timesJumped = 0;
			ticksSliding = 0;
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (hasSlide && shouldSlam) {
			slamTick(() -> {
				obj.getWorld().getOtherEntities(obj, new Box(obj.getBlockPos()).expand(5, 1, 5), foundEntity -> foundEntity.isAlive() && foundEntity.distanceTo(obj) < 5).forEach(entity -> {
					if (entity instanceof LivingEntity living && EnchancementUtil.shouldHurt(obj, living)) {
						living.takeKnockback(1, obj.getX() - living.getX(), obj.getZ() - living.getZ());
					}
				});
				obj.getWorld().emitGameEvent(GameEvent.STEP, obj.getPos(), GameEvent.Emitter.of(obj.getSteppingBlockState()));
			});
			EnchancementUtil.PACKET_IMMUNITIES.put(obj, 20);
		}
		EntityAttributeInstance attribute = obj.getAttributeInstance(StepHeightEntityAttributeMain.STEP_HEIGHT);
		ScaleData data = ModScaleTypes.SLIDE_HITBOX_TYPE.getScaleData(obj);
		if (hasSlide && isSliding()) {
			if (!attribute.hasModifier(STEP_HEIGHT_INCREASE)) {
				attribute.addPersistentModifier(STEP_HEIGHT_INCREASE);
			}
			if (data.getScale() != 1.5F) {
				data.setScale(1.5F);
			}
			EnchancementUtil.PACKET_IMMUNITIES.put(obj, 20);
		} else {
			if (attribute.hasModifier(STEP_HEIGHT_INCREASE)) {
				attribute.removeModifier(STEP_HEIGHT_INCREASE);
			}
			if (data.getScale() != 1) {
				data.setScale(1);
			}
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasSlide && !obj.isSpectator() && obj == MinecraftClient.getInstance().player) {
			if (shouldSlam) {
				slamTick(() -> {
					disallowSlide = true;
					BlockPos.Mutable mutable = new BlockPos.Mutable();
					for (int i = 0; i < 360; i += 15) {
						for (int j = 1; j < 5; j++) {
							double x = obj.getX() + MathHelper.sin(i) * j / 2, z = obj.getZ() + MathHelper.cos(i) * j / 2;
							BlockState state = obj.getWorld().getBlockState(mutable.set(x, Math.round(obj.getY() - 1), z));
							if (!state.isReplaceable() && obj.getWorld().getBlockState(mutable.move(Direction.UP)).isReplaceable()) {
								obj.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), x, mutable.getY(), z, 0, 0, 0);
							}
						}
					}
				});
			}
			GameOptions options = MinecraftClient.getInstance().options;
			if (!options.sprintKey.isPressed()) {
				disallowSlide = false;
			}
			boolean sneaking = options.sneakKey.isPressed();
			if (slamCooldown == 0 && !obj.isOnGround() && sneaking && !wasSneaking && !isSliding() && EnchancementUtil.isGroundedOrAirborne(obj)) {
				shouldSlam = true;
				slamCooldown = DEFAULT_SLAM_COOLDOWN;
				SlideSlamPacket.send();
			}
			wasSneaking = sneaking;
			if (options.sprintKey.isPressed() && !obj.isSneaking() && !disallowSlide) {
				if (!isSliding() && obj.isOnGround() && EnchancementUtil.isGroundedOrAirborne(obj)) {
					velocity = getVelocityFromInput(options).rotateY((float) Math.toRadians(-(obj.getHeadYaw() + 90)));
					SlideVelocityPacket.send(velocity);
				}
			} else if (velocity != Vec3d.ZERO) {
				velocity = Vec3d.ZERO;
				SlideVelocityPacket.send(velocity);
			}
		} else {
			disallowSlide = false;
			wasSneaking = false;
		}
	}

	public Vec3d getVelocity() {
		return velocity;
	}

	public void setVelocity(Vec3d velocity) {
		this.velocity = velocity;
	}

	public void setShouldSlam(boolean shouldSlam) {
		this.shouldSlam = shouldSlam;
	}

	public boolean shouldSlam() {
		return shouldSlam;
	}

	public void setJumpBoostResetTicks(int jumpBoostResetTicks) {
		this.jumpBoostResetTicks = jumpBoostResetTicks;
	}

	public void setSlamCooldown(int slamCooldown) {
		this.slamCooldown = slamCooldown;
	}

	public int getTimesJumped() {
		return timesJumped;
	}

	public boolean isSliding() {
		return !velocity.equals(Vec3d.ZERO);
	}

	public boolean shouldBoostJump() {
		return ticksLeftToJump > 0;
	}

	public float getJumpBonus() {
		return MathHelper.lerp(ticksSliding / 60F, 1, 3);
	}

	public boolean hasSlide() {
		return hasSlide;
	}

	private void slamTick(Runnable onLand) {
		obj.setVelocity(obj.getVelocity().getX() * 0.98, -3, obj.getVelocity().getZ() * 0.98);
		obj.velocityDirty = true;
		obj.fallDistance = 0;
		if (obj.isOnGround()) {
			shouldSlam = false;
			ticksLeftToJump = 5;
			if (timesJumped < 3) {
				timesJumped++;
			}
			obj.playSound(ModSoundEvents.ENTITY_GENERIC_IMPACT, 1, 1);
			onLand.run();
		}
	}

	private Vec3d getVelocityFromInput(GameOptions options) {
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
		return new Vec3d(any ? x : 1, 0, z).multiply(forward && sideways ? 0.75F : 1);
	}
}
