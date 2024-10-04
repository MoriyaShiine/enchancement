/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.client.payload.AddMovementBurstParticlesPayload;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.enchantment.effect.DirectionMovementBurstEffect;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.DirectionMovementBurstPayload;
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
	private int cooldown = 0, lastCooldown = 0;

	private boolean hasDirectionMovementBurst = false;

	private boolean wasPressingStrafeKey = false;
	private int ticksLeftToPressActivationKey = 0;

	public DirectionMovementBurstComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		cooldown = tag.getInt("Cooldown");
		lastCooldown = tag.getInt("LastCooldown");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putInt("Cooldown", cooldown);
		tag.putInt("LastCooldown", lastCooldown);
	}

	@Override
	public void tick() {
		int entityCooldown = DirectionMovementBurstEffect.getCooldown(obj);
		hasDirectionMovementBurst = entityCooldown > 0;
		if (hasDirectionMovementBurst) {
			if (cooldown > 0) {
				cooldown--;
			}
		} else {
			setCooldown(0);
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

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
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
		return cooldown == 0 && !obj.isSpectator();
	}

	public void use(double velocityX, double velocityZ) {
		obj.addVelocity(velocityX, 0, velocityZ);
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_STRAFE, 1, 1);
		setCooldown(DirectionMovementBurstEffect.getCooldown(obj));
	}

	@Environment(EnvType.CLIENT)
	private Vec3d getVelocityFromInput(GameOptions options) {
		float strength = DirectionMovementBurstEffect.getStrength(MinecraftClient.getInstance().player);
		if (options.forwardKey.isPressed()) {
			return new Vec3d(strength, 0, 0);
		}
		if (options.backKey.isPressed()) {
			return new Vec3d(-strength, 0, 0);
		}
		if (options.leftKey.isPressed()) {
			return new Vec3d(0, 0, -strength);
		}
		if (options.rightKey.isPressed()) {
			return new Vec3d(0, 0, strength);
		}
		return Vec3d.ZERO;
	}
}
