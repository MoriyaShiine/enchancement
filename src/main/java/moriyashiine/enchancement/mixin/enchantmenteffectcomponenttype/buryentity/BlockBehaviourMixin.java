/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.buryentity;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.BuryEntityComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
	@Inject(method = "affectNeighborsAfterRemoval", at = @At("HEAD"))
	private void enchancement$buryEntity(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston, CallbackInfo ci) {
		level.getEntitiesOfClass(LivingEntity.class, new AABB(pos), entity -> !entity.isDeadOrDying()).forEach(foundEntity -> {
			BuryEntityComponent buryEntityComponent = ModEntityComponents.BURY_ENTITY.get(foundEntity);
			if (buryEntityComponent.getBuryPos() != null && buryEntityComponent.getBuryPos().equals(pos)) {
				buryEntityComponent.unbury();
			}
		});
	}
}
