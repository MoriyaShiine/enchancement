/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.util.PushComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.RotationBurstPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.RotationBurstEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class RotationBurstComponent extends PushComponent {
	private int wavedashTicks = 0;

	private boolean wasPressingKey = false;
	private int ticksPressingJump = 0;

	public RotationBurstComponent(LivingEntity obj) {
		super(obj);
	}

	@Override
	public void readData(ValueInput input) {
		super.readData(input);
		wavedashTicks = input.getIntOr("WavedashTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		super.writeData(output);
		output.putInt("WavedashTicks", wavedashTicks);
	}

	@Override
	public void tick() {
		super.tick();
		if (hasEffect() && wavedashTicks > 0) {
			wavedashTicks--;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasEffect()) {
			LivingEntity controllingObj = getControllingObj();
			if (!controllingObj.isSpectator() && SLibClientUtils.isHost(controllingObj)) {
				if (controllingObj.jumping) {
					ticksPressingJump = Math.min(2, ++ticksPressingJump);
				} else {
					ticksPressingJump = 0;
				}
				boolean pressingKey = EnchancementClient.ROTATION_BURST_KEYMAPPING.isDown();
				if (pressingKey && !wasPressingKey && canUse()) {
					use();
					SLibClientUtils.addParticles(obj, ParticleTypes.CLOUD, 8, ParticleAnchor.BODY);
					RotationBurstPayload.send(obj);
				}
				wasPressingKey = pressingKey;
			}
		}
	}

	@Override
	public void sync() {
		ModEntityComponents.ROTATION_BURST.sync(obj);
	}

	@Override
	public DataComponentType<?> getEffectType() {
		return ModEnchantmentEffectComponentTypes.ROTATION_BURST;
	}

	@Override
	protected CooldownSupplier getCooldownSupplier() {
		return RotationBurstEffect::getCooldown;
	}

	@Override
	public void reset() {
		super.reset();
		wavedashTicks = 0;
		wasPressingKey = false;
		ticksPressingJump = 0;
	}

	public boolean shouldWavedash() {
		return ticksPressingJump < 2 && wavedashTicks > 0 && obj.onGround();
	}

	public boolean canUse() {
		return cooldown == 0 && !obj.onGround() && SLibUtils.isGroundedOrAirborne(obj);
	}

	public void use() {
		reset();
		wavedashTicks = RotationBurstEffect.getWavedashTicks(obj);
		if (obj.canSimulateMovement()) {
			Vec3 delta = obj.getLookAngle().normalize().scale(RotationBurstEffect.getStrength(obj)).scale(MultiplyMovementSpeedEvent.getMovementMultiplier(obj));
			delta = EnchancementUtil.modifyDeltaWithCurrent(obj, delta, 0.5);
			obj.setDeltaMovement(delta.x(), delta.y(), delta.z());
		}
		obj.playSound(ModSoundEvents.GENERIC_DASH, 1, 1);
		obj.gameEvent(GameEvent.ENTITY_ACTION);
		EnchancementUtil.resetFallDistance(obj);
	}
}
