/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.bury;

import moriyashiine.enchancement.common.component.entity.BuryComponent;
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
	private void enchancement$bury(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
		world.getEntitiesByClass(LivingEntity.class, new Box(pos), entity -> !entity.isDead()).forEach(foundEntity -> {
			BuryComponent buryComponent = ModEntityComponents.BURY.get(foundEntity);
			if (buryComponent.getBuryPos() != null && buryComponent.getBuryPos().equals(pos)) {
				buryComponent.unbury();
			}
		});
	}
}
