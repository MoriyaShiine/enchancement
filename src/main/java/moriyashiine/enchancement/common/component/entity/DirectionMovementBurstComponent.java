/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.client.payload.AddMovementBurstParticlesPayload;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.enchantment.effect.DirectionMovementBurstEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.DirectionMovementBurstPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class DirectionMovementBurstComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldRefresh = false;
	private int cooldown = 0, lastCooldown = 0, gravityTicks = 0;

	private boolean hasDirectionMovementBurst = false;

	private boolean wasPressingStrafeKey = false;
	private int ticksLeftToPressActivationKey = 0;

	public DirectionMovementBurstComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		shouldRefresh = tag.getBoolean("ShouldRefresh");
		cooldown = tag.getInt("Cooldown");
		lastCooldown = tag.getInt("LastCooldown");
		gravityTicks = tag.getInt("GravityTicks");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("ShouldRefresh", shouldRefresh);
		tag.putInt("Cooldown", cooldown);
		tag.putInt("LastCooldown", lastCooldown);
		tag.putInt("GravityTicks", gravityTicks);
	}

	@Override
	public void tick() {
		int entityCooldown = DirectionMovementBurstEffect.getCooldown(obj);
		hasDirectionMovementBurst = entityCooldown > 0;
		if (hasDirectionMovementBurst) {
			if (!shouldRefresh) {
				if (obj.isOnGround()) {
					shouldRefresh = true;
				}
			} else if (cooldown > 0) {
				cooldown--;
			}
			if (gravityTicks > 0) {
				gravityTicks--;
			}
		} else {
			shouldRefresh = false;
			setCooldown(0);
			gravityTicks = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasDirectionMovementBurst && canUse() && obj == MinecraftClient.getInstance().player) {
			GameOptions options = MinecraftClient.getInstance().options;
			boolean pressingStrafeKey = EnchancementClient.STRAFE_KEYBINDING.isPressed();
			if (ticksLeftToPressActivationKey > 0) {
				ticksLeftToPressActivationKey--;
			}
			if (pressingStrafeKey && !wasPressingStrafeKey) {
				if (ticksLeftToPressActivationKey > 0 || ModConfig.singlePressStrafe) {
					ticksLeftToPressActivationKey = 0;
					Vec3d inputVelocity = getVelocityFromInput(options);
					if (inputVelocity != Vec3d.ZERO) {
						Vec3d velocity = inputVelocity.rotateY((float) Math.toRadians(-(obj.getHeadYaw() + 90)));
						use(velocity.getX(), velocity.getZ());
						AddMovementBurstParticlesPayload.addParticles(obj);
						DirectionMovementBurstPayload.send(velocity);
					}
				} else {
					ticksLeftToPressActivationKey = 7;
				}
			}
			wasPressingStrafeKey = pressingStrafeKey;
		}
	}

	public void sync() {
		ModEntityComponents.DIRECTION_MOVEMENT_BURST.sync(obj);
	}

	public int getCooldown() {
		return cooldown;
	}

	private void setCooldown(int cooldown) {
		this.cooldown = cooldown;
		lastCooldown = cooldown;
	}

	public int getLastCooldown() {
		return lastCooldown;
	}

	public boolean hasDirectionMovementBurst() {
		return hasDirectionMovementBurst;
	}

	public boolean canUse() {
		return cooldown == 0 && EnchancementUtil.isGroundedOrAirborne(obj);
	}

	public boolean preventFalling() {
		return gravityTicks > 0;
	}

	public void use(double velocityX, double velocityZ) {
		reset();
		gravityTicks = 3;
		obj.setVelocity(velocityX, 0, velocityZ);
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_STRAFE, 1, 1);
		obj.fallDistance = 0;
		ModEntityComponents.AIR_MOBILITY.get(obj).resetTicksInAir();
	}

	public void reset() {
		setCooldown(DirectionMovementBurstEffect.getCooldown(obj));
		shouldRefresh = false;
	}

	@Environment(EnvType.CLIENT)
	private Vec3d getVelocityFromInput(GameOptions options) {
		float strength = obj.isOnGround() ? DirectionMovementBurstEffect.getGroundStrength(obj) : DirectionMovementBurstEffect.getAirStrength(obj);
		Vec3d velocity = Vec3d.ZERO;
		if (options.forwardKey.isPressed()) {
			velocity = new Vec3d(strength, 0, 0);
		}
		if (options.backKey.isPressed()) {
			velocity = new Vec3d(-strength, 0, 0);
		}
		if (options.leftKey.isPressed()) {
			velocity = new Vec3d(0, 0, -strength);
		}
		if (options.rightKey.isPressed()) {
			velocity = new Vec3d(0, 0, strength);
		}
		if (ModConfig.directionlessStrafe && !obj.isOnGround() && velocity.equals(Vec3d.ZERO)) {
			velocity = new Vec3d(strength, 0, 0);
		}
		return velocity;
	}
}
