/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.DirectionBurstPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.DirectionBurstEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class DirectionBurstComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final Player obj;
	private int cooldown = 0, lastCooldown = 0, gravityTicks = 0;

	private boolean hasDirectionBurst = false;

	private boolean wasPressingKey = false;
	private int ticksLeftToPressActivationKey = 0;

	public DirectionBurstComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		cooldown = input.getIntOr("Cooldown", 0);
		lastCooldown = input.getIntOr("LastCooldown", 0);
		gravityTicks = input.getIntOr("GravityTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putInt("Cooldown", cooldown);
		output.putInt("LastCooldown", lastCooldown);
		output.putInt("GravityTicks", gravityTicks);
	}

	@Override
	public void tick() {
		int entityCooldown = DirectionBurstEffect.getCooldown(obj);
		hasDirectionBurst = entityCooldown > 0;
		if (hasDirectionBurst) {
			if (cooldown > 0) {
				cooldown--;
			}
			if (gravityTicks > 0) {
				gravityTicks--;
			}
		} else {
			setCooldown(0);
			gravityTicks = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasDirectionBurst && canUse() && SLibClientUtils.isHost(obj)) {
			boolean pressingKey = EnchancementClient.DIRECTION_BURST_KEYMAPPING.isDown();
			if (ticksLeftToPressActivationKey > 0) {
				ticksLeftToPressActivationKey--;
			}
			if (pressingKey && !wasPressingKey) {
				if (!ModConfig.doublePressDirectionBurst || ticksLeftToPressActivationKey > 0) {
					ticksLeftToPressActivationKey = 0;
					Vec3 inputDelta = getDeltaMovementFromInput();
					if (inputDelta != Vec3.ZERO) {
						Vec3 delta = inputDelta.yRot((float) Math.toRadians(-(obj.getYHeadRot() + 90))).scale(MultiplyMovementSpeedEvent.getMovementMultiplier(obj));
						double x = delta.x(), z = delta.z();
						if (Double.compare(x, obj.getDeltaMovement().x()) * Math.signum(obj.getDeltaMovement().x()) > 0) {
							x += obj.getDeltaMovement().x();
						}
						if (Double.compare(z, obj.getDeltaMovement().z()) * Math.signum(obj.getDeltaMovement().z()) > 0) {
							z += obj.getDeltaMovement().z();
						}
						use(x, z);
						SLibClientUtils.addParticles(obj, ParticleTypes.CLOUD, 8, ParticleAnchor.BODY);
						DirectionBurstPayload.send(delta);
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

	public void use(double x, double z) {
		reset();
		if (!obj.onGround()) {
			gravityTicks = 3;
		}
		obj.setDeltaMovement(x, 0, z);
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_STRAFE, 1, 1);
		obj.gameEvent(GameEvent.ENTITY_ACTION);
		EnchancementUtil.resetFallDistance(obj);
		ModEntityComponents.AIR_MOBILITY.get(obj).resetTicksInAir();
	}

	public void reset() {
		setCooldown(DirectionBurstEffect.getCooldown(obj));
	}

	@Environment(EnvType.CLIENT)
	private Vec3 getDeltaMovementFromInput() {
		Options options = Minecraft.getInstance().options;
		float strength = obj.onGround() ? DirectionBurstEffect.getGroundStrength(obj) : DirectionBurstEffect.getAirStrength(obj);
		Vec3 delta = Vec3.ZERO;
		if (options.keyUp.isDown()) {
			delta = new Vec3(strength, 0, 0);
		}
		if (options.keyDown.isDown()) {
			delta = new Vec3(-strength, 0, 0);
		}
		if (options.keyLeft.isDown()) {
			delta = new Vec3(0, 0, -strength);
		}
		if (options.keyRight.isDown()) {
			delta = new Vec3(0, 0, strength);
		}
		if (ModConfig.inputlessDirectionBurst && !obj.onGround() && delta.equals(Vec3.ZERO)) {
			delta = new Vec3(strength, 0, 0);
		}
		return delta;
	}
}
