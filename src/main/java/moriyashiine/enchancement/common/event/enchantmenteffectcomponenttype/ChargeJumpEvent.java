/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ChargeJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.event.ModifyMovementEvents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class ChargeJumpEvent implements ModifyMovementEvents.JumpDelta {
	private static final BlockParticleOption SLIME_PARTICLE = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState());

	@Override
	public Vec3 modify(Vec3 delta, LivingEntity entity) {
		ChargeJumpComponent chargeJumpComponent = ModEntityComponents.CHARGE_JUMP.getNullable(entity);
		if (chargeJumpComponent != null && chargeJumpComponent.hasChargeJump() && chargeJumpComponent.isPressingChargeJump()) {
			double progress = chargeJumpComponent.getChargeProgress();
			double boost = chargeJumpComponent.getBoost();
			if (progress >= 2 / 18F && entity.level() instanceof ServerLevel level) {
				SLibUtils.playSound(entity, SoundEvents.SLIME_BLOCK_FALL);
				entity.gameEvent(GameEvent.ENTITY_ACTION);
				level.sendParticles(SLIME_PARTICLE, entity.getX(), entity.getY(), entity.getZ(), 32, 0, 0, 0, 0.15);
			}
			return delta.add(0, boost, 0);
		}
		return delta;
	}
}
