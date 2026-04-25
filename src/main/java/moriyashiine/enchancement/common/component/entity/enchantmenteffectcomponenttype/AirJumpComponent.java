/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.AirJumpPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.AirJumpEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class AirJumpComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final Player obj;
	private boolean shouldRefresh = false;
	private int cooldown = 0, lastCooldown = 0, jumpCooldown = 10, jumpsLeft = 0, ticksInAir = 0;

	private int maxJumps = 0;
	private boolean hasAirJump = false;

	private boolean wasJumping = false;

	public AirJumpComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		shouldRefresh = input.getBooleanOr("ShouldRefresh", false);
		cooldown = input.getIntOr("Cooldown", 0);
		lastCooldown = input.getIntOr("LastCooldown", 0);
		jumpCooldown = input.getIntOr("JumpCooldown", 10);
		jumpsLeft = input.getIntOr("JumpsLeft", 0);
		ticksInAir = input.getIntOr("TicksInAir", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("ShouldRefresh", shouldRefresh);
		output.putInt("Cooldown", cooldown);
		output.putInt("LastCooldown", lastCooldown);
		output.putInt("JumpCooldown", jumpCooldown);
		output.putInt("JumpsLeft", jumpsLeft);
		output.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void tick() {
		int entityCooldown = AirJumpEffect.getChargeCooldown(obj);
		maxJumps = AirJumpEffect.getAirJumps(obj);
		hasAirJump = maxJumps > 0;
		if (hasAirJump) {
			if (!shouldRefresh) {
				if (obj.onGround()) {
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
			if (obj.onGround()) {
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
		return jumpCooldown == 0 && jumpsLeft > 0 && ticksInAir >= 5 && !obj.onGround() && SLibUtils.isGroundedOrAirborne(obj) && !ModEntityComponents.BOOST_IN_FLUID.get(obj).blocksAirEffects();
	}

	public void use() {
		if (cooldown == 0 || jumpsLeft == maxJumps) {
			setCooldown(AirJumpEffect.getChargeCooldown(obj));
		}
		shouldRefresh = false;
		jumpCooldown = AirJumpEffect.getJumpCooldown(obj);
		jumpsLeft--;
		obj.setDeltaMovement(obj.getDeltaMovement().x(), MultiplyMovementSpeedEvent.getJumpStrength(obj, AirJumpEffect.getAirJumpStrength(obj)), obj.getDeltaMovement().z());
		if (obj.isSprinting()) {
			float rad = (float) Math.toRadians(obj.getYRot());
			obj.addDeltaMovement(new Vec3(-Mth.sin(rad) * 0.2, 0, Mth.cos(rad) * 0.2));
		}
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_AIR_JUMP, 1, 1);
		obj.gameEvent(GameEvent.ENTITY_ACTION);
		EnchancementUtil.resetFallDistance(obj);
	}

	public void reset() {
		setCooldown(AirJumpEffect.getChargeCooldown(obj));
		jumpsLeft = 0;
	}
}
