/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.util.PushComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.AirJumpPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.AirJumpEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class AirJumpComponent extends PushComponent {
	private static final int DEFAULT_JUMP_COOLDOWN = 10;

	private int jumpCooldown = DEFAULT_JUMP_COOLDOWN, jumpsLeft = 0, ticksInAir = 0;

	private int lastTickedCooldown = 0;

	private boolean wasJumping = false;

	public AirJumpComponent(LivingEntity obj) {
		super(obj);
	}

	@Override
	public void readData(ValueInput input) {
		super.readData(input);
		jumpCooldown = input.getIntOr("JumpCooldown", DEFAULT_JUMP_COOLDOWN);
		jumpsLeft = input.getIntOr("JumpsLeft", 0);
		ticksInAir = input.getIntOr("TicksInAir", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		super.writeData(output);
		output.putInt("JumpCooldown", jumpCooldown);
		output.putInt("JumpsLeft", jumpsLeft);
		output.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void tick() {
		super.tick();
		if (hasEffect()) {
			if (lastTickedCooldown != 0 && cooldown == 0 && jumpsLeft < getMaxJumps()) {
				jumpsLeft++;
				setCooldown(entityCooldown);
			}
			if (jumpCooldown > 0) {
				jumpCooldown--;
			}
			if (obj.onGround()) {
				ticksInAir = 0;
			} else {
				ticksInAir++;
			}
			if (ModEntityComponents.WALL_JUMP.get(obj).isSliding()) {
				jumpCooldown = AirJumpEffect.getJumpCooldown(obj);
			}
		}
		lastTickedCooldown = cooldown;
	}

	@Override
	public void clientTick() {
		tick();
		if (hasEffect()) {
			LivingEntity controllingObj = getControllingObj();
			if (!controllingObj.isSpectator() && SLibClientUtils.isHost(controllingObj)) {
				if (controllingObj.jumping && !wasJumping && canUse()) {
					use();
					SLibClientUtils.addParticles(obj, ParticleTypes.CLOUD, 8, ParticleAnchor.BASE);
					AirJumpPayload.send(obj);
				}
				wasJumping = controllingObj.jumping;
			}
		}
	}

	@Override
	public void sync() {
		ModEntityComponents.AIR_JUMP.sync(obj);
	}

	@Override
	public DataComponentType<?> getEffectType() {
		return ModEnchantmentEffectComponentTypes.AIR_JUMP;
	}

	@Override
	protected CooldownSupplier getCooldownSupplier() {
		return AirJumpEffect::getChargeCooldown;
	}

	@Override
	public void reset() {
		super.reset();
		jumpCooldown = DEFAULT_JUMP_COOLDOWN;
		jumpsLeft = ticksInAir = 0;
		wasJumping = false;
	}

	public int getJumpsLeft() {
		return jumpsLeft;
	}

	public int getMaxJumps() {
		return AirJumpEffect.getAirJumps(obj);
	}

	public boolean canUse() {
		return jumpCooldown == 0 && jumpsLeft > 0 && ticksInAir >= 5 && !obj.onGround() && SLibUtils.isGroundedOrAirborne(obj) && !ModEntityComponents.BOOST_IN_FLUID.get(obj).blocksAirEffects();
	}

	public void use() {
		shouldRefresh = false;
		if (cooldown == 0 || getJumpsLeft() == getMaxJumps()) {
			setCooldown(AirJumpEffect.getChargeCooldown(obj));
		}
		jumpCooldown = AirJumpEffect.getJumpCooldown(obj);
		jumpsLeft--;
		if (obj.canSimulateMovement()) {
			obj.setDeltaMovement(obj.getDeltaMovement().x(), MultiplyMovementSpeedEvent.getJumpStrength(obj, AirJumpEffect.getAirJumpStrength(obj)), obj.getDeltaMovement().z());
			if (obj.isSprinting()) {
				float rad = (float) Math.toRadians(obj.getYRot());
				obj.addDeltaMovement(new Vec3(-Mth.sin(rad) * 0.2, 0, Mth.cos(rad) * 0.2));
			}
		}
		obj.playSound(ModSoundEvents.GENERIC_AIR_JUMP, 1, 1);
		obj.gameEvent(GameEvent.ENTITY_ACTION);
		EnchancementUtil.resetFallDistance(obj);
	}
}
