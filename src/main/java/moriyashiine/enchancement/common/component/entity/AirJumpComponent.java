/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.enchantment.effect.AirJumpEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.AirJumpPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.accessor.LivingEntityAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class AirJumpComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldRefresh = false;
	private int cooldown = 0, lastCooldown = 0, jumpCooldown = 10, jumpsLeft = 0, ticksInAir = 0;

	private int maxJumps = 0;
	private boolean hasAirJump = false;

	public AirJumpComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		shouldRefresh = tag.getBoolean("ShouldRefresh");
		cooldown = tag.getInt("Cooldown");
		lastCooldown = tag.getInt("LastCooldown");
		jumpCooldown = tag.getInt("JumpCooldown");
		jumpsLeft = tag.getInt("JumpsLeft");
		ticksInAir = tag.getInt("TicksInAir");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("ShouldRefresh", shouldRefresh);
		tag.putInt("Cooldown", cooldown);
		tag.putInt("LastCooldown", lastCooldown);
		tag.putInt("JumpCooldown", jumpCooldown);
		tag.putInt("JumpsLeft", jumpsLeft);
		tag.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void tick() {
		int entityCooldown = AirJumpEffect.getChargeCooldown(obj);
		maxJumps = AirJumpEffect.getAirJumps(obj);
		hasAirJump = maxJumps > 0;
		if (hasAirJump) {
			if (!shouldRefresh) {
				if (obj.isOnGround()) {
					shouldRefresh = true;
				}
			} else if (cooldown > 0) {
				cooldown--;
				if (cooldown == 0 && jumpsLeft < maxJumps) {
					jumpsLeft++;
					setCooldown(entityCooldown);
				}
			}
			if (jumpCooldown > 0) {
				jumpCooldown--;
			}
			if (obj.isOnGround()) {
				ticksInAir = 0;
			} else {
				ticksInAir++;
			}
		} else {
			shouldRefresh = false;
			cooldown = 0;
			jumpCooldown = 0;
			jumpsLeft = 0;
			ticksInAir = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasAirJump && ((LivingEntityAccessor) obj).enchancement$jumping() && canUse()) {
			use();
			addParticles(obj);
			AirJumpPayload.send();
		}
	}

	public void sync() {
		ModEntityComponents.AIR_JUMP.sync(obj);
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
		lastCooldown = cooldown;
	}

	public int getCooldown() {
		return cooldown;
	}

	public int getLastCooldown() {
		return lastCooldown;
	}

	public int getJumpsLeft() {
		return jumpsLeft;
	}

	public void setJumpsLeft(int jumpsLeft) {
		this.jumpsLeft = jumpsLeft;
	}

	public int getMaxJumps() {
		return maxJumps;
	}

	public boolean hasAirJump() {
		return hasAirJump;
	}

	public boolean canUse() {
		int entityJumpCooldown = AirJumpEffect.getJumpCooldown(obj);
		return jumpCooldown == 0 && jumpsLeft > 0 && ticksInAir >= (obj.getWorld().isClient ? entityJumpCooldown : entityJumpCooldown - 1) && !obj.isOnGround() && EnchancementUtil.isGroundedOrAirborne(obj);
	}

	public void use() {
		obj.jump();
		obj.setVelocity(obj.getVelocity().getX(), obj.getVelocity().getY() * AirJumpEffect.getAirJumpStrength(obj), obj.getVelocity().getZ());
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_AIR_JUMP, 1, 1);
		if (cooldown == 0 || jumpsLeft == maxJumps) {
			setCooldown(AirJumpEffect.getChargeCooldown(obj));
		}
		shouldRefresh = false;
		jumpCooldown = AirJumpEffect.getJumpCooldown(obj);
		jumpsLeft--;
	}

	public static void addParticles(Entity entity) {
		if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity) {
			for (int i = 0; i < 8; i++) {
				entity.getWorld().addParticle(ParticleTypes.CLOUD, entity.getParticleX(1), entity.getY(), entity.getParticleZ(1), 0, 0, 0);
			}
		}
	}
}
