/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin {
	@Shadow
	public abstract Block getBlock();

	@Shadow
	public abstract FluidState getFluidState();

	@ModifyReturnValue(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("RETURN"))
	private VoxelShape enchancement$fluidWalking(VoxelShape original, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (original.isEmpty() && !getFluidState().isEmpty() && !(getBlock() instanceof LiquidBlock) && context instanceof EntityCollisionContext entityCollisionContext) {
			Entity entity = entityCollisionContext.getEntity();
			if (entity != null && !entity.isUnderWater() && Mth.frac(entity.getY()) >= 0.5 && EnchancementUtil.shouldFluidWalk(entity) && level.getFluidState(pos.above(Mth.ceil(entity.getBbHeight()) - 1)).isEmpty()) {
				return EnchancementUtil.FLUID_WALKING_SHAPE;
			}
		}
		return original;
	}
}
