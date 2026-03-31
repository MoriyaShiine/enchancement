/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.RotationBurstPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.RotationBurstEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class RotationBurstComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final Player obj;
	private boolean shouldRefresh = false;
	private int cooldown = 0, lastCooldown = 0, wavedashTicks = 0;

	private boolean hasRotationBurst = false, wasPressingKey = false;
	private int resetDelayTicks = 0, ticksPressingJump = 0;

	public RotationBurstComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		shouldRefresh = input.getBooleanOr("ShouldRefresh", false);
		cooldown = input.getIntOr("Cooldown", 0);
		lastCooldown = input.getIntOr("LastCooldown", 0);
		wavedashTicks = input.getIntOr("WavedashTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("ShouldRefresh", shouldRefresh);
		output.putInt("Cooldown", cooldown);
		output.putInt("LastCooldown", lastCooldown);
		output.putInt("WavedashTicks", wavedashTicks);
	}

	@Override
	public void tick() {
		int entityCooldown = RotationBurstEffect.getCooldown(obj);
		hasRotationBurst = entityCooldown > 0;
		if (hasRotationBurst) {
			if (!shouldRefresh) {
				if (obj.onGround()) {
					shouldRefresh = true;
				}
			} else if (cooldown > 0) {
				cooldown--;
			}
			if (wavedashTicks > 0) {
				wavedashTicks--;
			}
			if (resetDelayTicks > 0) {
				resetDelayTicks--;
			}
		} else {
			shouldRefresh = false;
			setCooldown(0);
			wavedashTicks = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasRotationBurst && !obj.isSpectator() && SLibClientUtils.isHost(obj)) {
			if (obj.jumping) {
				ticksPressingJump = Math.min(2, ++ticksPressingJump);
			} else {
				ticksPressingJump = 0;
			}
			boolean pressingKey = EnchancementClient.ROTATION_BURST_KEYMAPPING.isDown();
			if (pressingKey && !wasPressingKey && canUse()) {
				use();
				SLibClientUtils.addParticles(obj, ParticleTypes.CLOUD, 8, ParticleAnchor.BODY);
				RotationBurstPayload.send();
			}
			wasPressingKey = pressingKey;
		} else {
			wasPressingKey = false;
			ticksPressingJump = 0;
		}
	}

	public void sync() {
		ModEntityComponents.ROTATION_BURST.sync(obj);
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

	public boolean hasRotationBurst() {
		return hasRotationBurst;
	}

	public boolean shouldWavedash() {
		return ticksPressingJump < 2 && wavedashTicks > 0 && obj.onGround();
	}

	public boolean canUse() {
		return Math.max(cooldown, resetDelayTicks) == 0 && !obj.onGround() && SLibUtils.isGroundedOrAirborne(obj);
	}

	public void use() {
		reset();
		wavedashTicks = RotationBurstEffect.getWavedashTicks(obj);
		Vec3 delta = obj.getLookAngle().normalize().scale(RotationBurstEffect.getStrength(obj)).scale(MultiplyMovementSpeedEvent.getMovementMultiplier(obj));
		obj.setDeltaMovement(delta.x(), delta.y(), delta.z());
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_DASH, 1, 1);
		obj.gameEvent(GameEvent.ENTITY_ACTION);
		EnchancementUtil.resetFallDistance(obj);
	}

	public void reset() {
		setCooldown(RotationBurstEffect.getCooldown(obj));
		shouldRefresh = false;
	}

	public void markDelay() {
		resetDelayTicks = 3;
	}
}
