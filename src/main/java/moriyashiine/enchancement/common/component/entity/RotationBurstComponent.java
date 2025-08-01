/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.enchantment.effect.RotationBurstEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.RotationBurstPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class RotationBurstComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldRefresh = false;
	private int cooldown = 0, lastCooldown = 0, wavedashTicks = 0;

	private boolean hasRotationBurst = false, wasPressingKey = false;
	private int resetDelayTicks = 0, ticksPressingJump = 0;

	public RotationBurstComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		shouldRefresh = readView.getBoolean("ShouldRefresh", false);
		cooldown = readView.getInt("Cooldown", 0);
		lastCooldown = readView.getInt("LastCooldown", 0);
		wavedashTicks = readView.getInt("WavedashTicks", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.putBoolean("ShouldRefresh", shouldRefresh);
		writeView.putInt("Cooldown", cooldown);
		writeView.putInt("LastCooldown", lastCooldown);
		writeView.putInt("WavedashTicks", wavedashTicks);
	}

	@Override
	public void tick() {
		int entityCooldown = RotationBurstEffect.getCooldown(obj);
		hasRotationBurst = entityCooldown > 0;
		if (hasRotationBurst) {
			if (!shouldRefresh) {
				if (obj.isOnGround()) {
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
			boolean pressingKey = EnchancementClient.ROTATION_BURST_KEYBINDING.isPressed();
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
		return ticksPressingJump < 2 && wavedashTicks > 0 && obj.isOnGround();
	}

	public boolean canUse() {
		return Math.max(cooldown, resetDelayTicks) == 0 && !obj.isOnGround() && SLibUtils.isGroundedOrAirborne(obj);
	}

	public void use() {
		reset();
		wavedashTicks = RotationBurstEffect.getWavedashTicks(obj);
		Vec3d velocity = obj.getRotationVector().normalize().multiply(RotationBurstEffect.getStrength(obj)).multiply(MultiplyMovementSpeedEvent.getMovementMultiplier(obj));
		obj.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_DASH, 1, 1);
		obj.emitGameEvent(GameEvent.ENTITY_ACTION);
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
