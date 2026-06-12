/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.api.event.CappedMultiplyDeltaMovementEvent;
import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.util.PushComponent;
import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.init.EnchancementSoundEvents;
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
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class DirectionBurstComponent extends PushComponent {
	private int gravityTicks = 0;

	private boolean wasPressingKey = false;
	private int ticksLeftToPressActivationKey = 0;

	public DirectionBurstComponent(LivingEntity obj) {
		super(obj);
	}

	@Override
	public void readData(ValueInput input) {
		super.readData(input);
		gravityTicks = input.getIntOr("GravityTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		super.writeData(output);
		output.putInt("GravityTicks", gravityTicks);
	}

	@Override
	public void tick() {
		super.tick();
		if (hasEffect() && gravityTicks > 0) {
			gravityTicks--;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasEffect()) {
			LivingEntity controllingObj = getControllingObj();
			if (!controllingObj.isSpectator() && SLibClientUtils.isHost(controllingObj)) {
				boolean pressingKey = EnchancementClient.DIRECTION_BURST_KEYMAPPING.isDown();
				if (ticksLeftToPressActivationKey > 0) {
					ticksLeftToPressActivationKey--;
				}
				if (pressingKey && !wasPressingKey && canUse()) {
					if (!EnchancementConfig.doublePressDirectionBurst || ticksLeftToPressActivationKey > 0) {
						ticksLeftToPressActivationKey = 0;
						Vec3 inputDelta = getDeltaMovementFromInput();
						if (inputDelta != Vec3.ZERO) {
							Vec3 delta = createDelta(inputDelta);
							use(delta);
							SLibClientUtils.addParticles(obj, ParticleTypes.CLOUD, 8, ParticleAnchor.BODY);
							DirectionBurstPayload.send(obj, delta);
						}
					} else {
						ticksLeftToPressActivationKey = 7;
					}
				}
				wasPressingKey = pressingKey;
			}
		}
	}

	@Override
	public void sync() {
		EnchancementEntityComponents.DIRECTION_BURST.sync(obj);
	}

	@Override
	public DataComponentType<?> getEffectType() {
		return EnchancementEnchantmentEffectComponentTypes.DIRECTION_BURST;
	}

	@Override
	protected CooldownSupplier getCooldownSupplier() {
		return DirectionBurstEffect::getCooldown;
	}

	@Override
	protected boolean updateRefresh() {
		return true;
	}

	@Override
	public void reset() {
		super.reset();
		gravityTicks = 0;
		wasPressingKey = false;
		ticksLeftToPressActivationKey = 0;
	}

	public boolean canUse() {
		return cooldown == 0 && SLibUtils.hasNormalMovement(obj, obj.is(EntityTypeTags.AQUATIC));
	}

	public boolean preventFalling() {
		return gravityTicks > 0;
	}

	public void use(Vec3 delta) {
		reset();
		if (!obj.onGround()) {
			gravityTicks = 3;
		}
		if (obj.canSimulateMovement()) {
			obj.setDeltaMovement(delta.x(), 0, delta.z());
		}
		obj.playSound(EnchancementSoundEvents.GENERIC_STRAFE, 1, 1);
		obj.gameEvent(GameEvent.ENTITY_ACTION);
		EnchancementUtil.resetFallDistance(obj);
		EnchancementEntityComponents.AIR_MOBILITY.get(obj).resetTicksInAir();
	}

	public Vec3 createDelta(Vec3 inputDelta) {
		Vec3 delta = inputDelta.yRot((float) Math.toRadians(-(obj.getYHeadRot() + 90))).scale(CappedMultiplyDeltaMovementEvent.getMovementMultiplier(obj, 0.5F));
		return EnchancementUtil.modifyDeltaWithCurrent(obj, delta, 0.5);
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
		if (EnchancementConfig.inputlessDirectionBurst && !obj.onGround() && delta.equals(Vec3.ZERO)) {
			delta = new Vec3(strength, 0, 0);
		}
		return delta;
	}
}
