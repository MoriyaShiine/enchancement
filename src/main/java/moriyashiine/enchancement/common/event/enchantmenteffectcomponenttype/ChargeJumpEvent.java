/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.ChargeJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.event.ModifyJumpVelocityEvent;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class ChargeJumpEvent implements ModifyJumpVelocityEvent {
	private static final BlockStateParticleEffect SLIME_PARTICLE = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.getDefaultState());

	@Override
	public Vec3d modify(Vec3d velocity, LivingEntity entity) {
		ChargeJumpComponent chargeJumpComponent = ModEntityComponents.CHARGE_JUMP.getNullable(entity);
		if (chargeJumpComponent != null && chargeJumpComponent.hasChargeJump()) {
			float boostProgress = chargeJumpComponent.getChargeProgress();
			if (boostProgress > 0) {
				if (entity.getWorld() instanceof ServerWorld world) {
					SLibUtils.playSound(entity, SoundEvents.BLOCK_SLIME_BLOCK_FALL);
					world.spawnParticles(SLIME_PARTICLE, entity.getX(), entity.getY(), entity.getZ(), 32, 0, 0, 0, 0.15);
				}
				return velocity.add(0, chargeJumpComponent.getBoost(), 0);
			}
		}
		return velocity;
	}
}
