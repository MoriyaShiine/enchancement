/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.buryentity;

import moriyashiine.enchancement.common.component.entity.BuryEntityComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
	@Inject(method = "onStateReplaced", at = @At("HEAD"))
	private void enchancement$buryEntity(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
		world.getEntitiesByClass(LivingEntity.class, new Box(pos), entity -> !entity.isDead()).forEach(foundEntity -> {
			BuryEntityComponent buryEntityComponent = ModEntityComponents.BURY_ENTITY.get(foundEntity);
			if (buryEntityComponent.getBuryPos() != null && buryEntityComponent.getBuryPos().equals(pos)) {
				buryEntityComponent.unbury();
			}
		});
	}
}
