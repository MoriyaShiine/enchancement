/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.client.payload.AddMovementBurstParticlesPayload;
import moriyashiine.enchancement.common.enchantment.effect.RotationBurstEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.RotationBurstPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class RotationBurstComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldRefresh = false;
	private int cooldown = 0, lastCooldown = 0, wavedashTicks = 0;

	private boolean hasRotationBurst = false, wasPressingKey = false;
	private int ticksPressingJump = 0;

	public RotationBurstComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		shouldRefresh = tag.getBoolean("ShouldRefresh");
		cooldown = tag.getInt("Cooldown");
		lastCooldown = tag.getInt("LastCooldown");
		wavedashTicks = tag.getInt("WavedashTicks");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("ShouldRefresh", shouldRefresh);
		tag.putInt("Cooldown", cooldown);
		tag.putInt("LastCooldown", lastCooldown);
		tag.putInt("WavedashTicks", wavedashTicks);
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
		} else {
			shouldRefresh = false;
			setCooldown(0);
			wavedashTicks = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasRotationBurst && !obj.isSpectator() && obj == MinecraftClient.getInstance().player) {
			if (obj.jumping) {
				ticksPressingJump = Math.min(2, ++ticksPressingJump);
			} else {
				ticksPressingJump = 0;
			}
			boolean pressingKey = EnchancementClient.ROTATION_BURST_KEYBINDING.isPressed();
			if (pressingKey && !wasPressingKey && canUse()) {
				use();
				AddMovementBurstParticlesPayload.addParticles(obj);
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
		return cooldown == 0 && !obj.isOnGround() && EnchancementUtil.isGroundedOrAirborne(obj);
	}

	public void use() {
		reset();
		wavedashTicks = RotationBurstEffect.getWavedashTicks(obj);
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_DASH, 1, 1);
		Vec3d velocity = obj.getRotationVector().normalize().multiply(RotationBurstEffect.getStrength(obj)).multiply(MultiplyMovementSpeedEvent.getMovementMultiplier(obj));
		obj.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
		obj.fallDistance = 0;
	}

	public void reset() {
		setCooldown(RotationBurstEffect.getCooldown(obj));
		shouldRefresh = false;
	}
}
