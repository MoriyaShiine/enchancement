/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.enchantment.effect.DirectionBurstEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.DirectionBurstPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class DirectionBurstComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldRefresh = false;
	private int cooldown = 0, lastCooldown = 0, gravityTicks = 0;

	private boolean hasDirectionBurst = false;

	private boolean wasPressingKey = false;
	private int ticksLeftToPressActivationKey = 0;

	public DirectionBurstComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		shouldRefresh = tag.getBoolean("ShouldRefresh", false);
		cooldown = tag.getInt("Cooldown", 0);
		lastCooldown = tag.getInt("LastCooldown", 0);
		gravityTicks = tag.getInt("GravityTicks", 0);
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
		int entityCooldown = DirectionBurstEffect.getCooldown(obj);
		hasDirectionBurst = entityCooldown > 0;
		if (hasDirectionBurst) {
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
		if (hasDirectionBurst && canUse() && SLibClientUtils.isHost(obj)) {
			GameOptions options = MinecraftClient.getInstance().options;
			boolean pressingKey = EnchancementClient.DIRECTION_BURST_KEYBINDING.isPressed();
			if (ticksLeftToPressActivationKey > 0) {
				ticksLeftToPressActivationKey--;
			}
			if (pressingKey && !wasPressingKey) {
				if (!ModConfig.doublePressDirectionBurst || ticksLeftToPressActivationKey > 0) {
					ticksLeftToPressActivationKey = 0;
					Vec3d inputVelocity = getVelocityFromInput(options);
					if (inputVelocity != Vec3d.ZERO) {
						Vec3d velocity = inputVelocity.rotateY((float) Math.toRadians(-(obj.getHeadYaw() + 90))).multiply(MultiplyMovementSpeedEvent.getMovementMultiplier(obj));
						use(velocity.getX(), velocity.getZ());
						SLibClientUtils.addParticles(obj, ParticleTypes.CLOUD, 8, ParticleAnchor.BODY);
						DirectionBurstPayload.send(velocity);
					}
				} else {
					ticksLeftToPressActivationKey = 7;
				}
			}
			wasPressingKey = pressingKey;
		}
	}

	public void sync() {
		ModEntityComponents.DIRECTION_BURST.sync(obj);
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

	public boolean hasDirectionBurst() {
		return hasDirectionBurst;
	}

	public boolean canUse() {
		return cooldown == 0 && SLibUtils.isGroundedOrAirborne(obj);
	}

	public boolean preventFalling() {
		return gravityTicks > 0;
	}

	public void use(double velocityX, double velocityZ) {
		reset();
		gravityTicks = 3;
		obj.setVelocity(velocityX, 0, velocityZ);
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_STRAFE, 1, 1);
		obj.emitGameEvent(GameEvent.ENTITY_ACTION);
		EnchancementUtil.resetFallDistance(obj);
		ModEntityComponents.AIR_MOBILITY.get(obj).resetTicksInAir();
	}

	public void reset() {
		setCooldown(DirectionBurstEffect.getCooldown(obj));
		shouldRefresh = false;
	}

	@Environment(EnvType.CLIENT)
	private Vec3d getVelocityFromInput(GameOptions options) {
		float strength = obj.isOnGround() ? DirectionBurstEffect.getGroundStrength(obj) : DirectionBurstEffect.getAirStrength(obj);
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
		if (ModConfig.inputlessDirectionBurst && !obj.isOnGround() && velocity.equals(Vec3d.ZERO)) {
			velocity = new Vec3d(strength, 0, 0);
		}
		return velocity;
	}
}
