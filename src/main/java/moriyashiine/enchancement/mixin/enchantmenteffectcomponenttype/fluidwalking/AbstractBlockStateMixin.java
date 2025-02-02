/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
	@Unique
	private static final VoxelShape OFFSET = Block.createCuboidShape(0, 0, 0, 16, 8, 16);

	@Shadow
	public abstract Block getBlock();

	@Shadow
	public abstract FluidState getFluidState();

	@ModifyReturnValue(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("RETURN"))
	private VoxelShape enchancement$fluidWalking(VoxelShape original, BlockView world, BlockPos pos, ShapeContext context) {
		if (original.isEmpty() && !getFluidState().isEmpty() && !(getBlock() instanceof FluidBlock) && context instanceof EntityShapeContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if (entity != null && !entity.isSubmergedInWater() && MathHelper.fractionalPart(entity.getY()) >= 0.5 && EnchancementUtil.shouldFluidWalk(entity) && world.getFluidState(pos.up(MathHelper.ceil(entity.getHeight()) - 1)).isEmpty()) {
				return OFFSET;
			}
		}
		return original;
	}
}
