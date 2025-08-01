/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.enchantment.effect.AirJumpEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.AirJumpPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class AirJumpComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldRefresh = false;
	private int cooldown = 0, lastCooldown = 0, jumpCooldown = 10, jumpsLeft = 0, ticksInAir = 0;

	private int maxJumps = 0;
	private boolean hasAirJump = false;

	private boolean wasJumping = false;

	public AirJumpComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		shouldRefresh = readView.getBoolean("ShouldRefresh", false);
		cooldown = readView.getInt("Cooldown", 0);
		lastCooldown = readView.getInt("LastCooldown", 0);
		jumpCooldown = readView.getInt("JumpCooldown", 0);
		jumpsLeft = readView.getInt("JumpsLeft", 0);
		ticksInAir = readView.getInt("TicksInAir", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.putBoolean("ShouldRefresh", shouldRefresh);
		writeView.putInt("Cooldown", cooldown);
		writeView.putInt("LastCooldown", lastCooldown);
		writeView.putInt("JumpCooldown", jumpCooldown);
		writeView.putInt("JumpsLeft", jumpsLeft);
		writeView.putInt("TicksInAir", ticksInAir);
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
			setCooldown(0);
			jumpCooldown = 0;
			jumpsLeft = 0;
			ticksInAir = 0;
		}
		if (ModEntityComponents.WALL_JUMP.get(obj).isSliding()) {
			jumpCooldown = AirJumpEffect.getJumpCooldown(obj);
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasAirJump && obj.jumping && !wasJumping && canUse()) {
			use();
			SLibClientUtils.addParticles(obj, ParticleTypes.CLOUD, 8, ParticleAnchor.BASE);
			AirJumpPayload.send();
		}
		wasJumping = obj.jumping;
	}

	public void sync() {
		ModEntityComponents.AIR_JUMP.sync(obj);
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

	public int getJumpsLeft() {
		return jumpsLeft;
	}

	public int getMaxJumps() {
		return maxJumps;
	}

	public boolean hasAirJump() {
		return hasAirJump;
	}

	public boolean canUse() {
		return jumpCooldown == 0 && jumpsLeft > 0 && ticksInAir >= 5 && !obj.isOnGround() && SLibUtils.isGroundedOrAirborne(obj);
	}

	public void use() {
		if (cooldown == 0 || jumpsLeft == maxJumps) {
			setCooldown(AirJumpEffect.getChargeCooldown(obj));
		}
		shouldRefresh = false;
		jumpCooldown = AirJumpEffect.getJumpCooldown(obj);
		jumpsLeft--;
		obj.setVelocity(obj.getVelocity().getX(), MultiplyMovementSpeedEvent.getJumpStrength(obj, AirJumpEffect.getAirJumpStrength(obj)), obj.getVelocity().getZ());
		if (obj.isSprinting()) {
			float rad = (float) Math.toRadians(obj.getYaw());
			obj.addVelocityInternal(new Vec3d(-MathHelper.sin(rad) * 0.2, 0, MathHelper.cos(rad) * 0.2));
		}
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_AIR_JUMP, 1, 1);
		obj.emitGameEvent(GameEvent.ENTITY_ACTION);
		EnchancementUtil.resetFallDistance(obj);
	}

	public void reset() {
		setCooldown(AirJumpEffect.getChargeCooldown(obj));
		jumpsLeft = 0;
	}
}
