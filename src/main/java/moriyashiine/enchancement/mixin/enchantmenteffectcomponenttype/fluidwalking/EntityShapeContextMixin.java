/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(EntityShapeContext.class)
public class EntityShapeContextMixin {
	@Shadow
	@Final
	@Mutable
	private Predicate<FluidState> walkOnFluidPredicate;

	@Inject(method = "<init>(ZDLnet/minecraft/item/ItemStack;Ljava/util/function/Predicate;Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
	protected void enchancement$fluidWalking(boolean descending, double minY, ItemStack heldItem, Predicate<LivingEntity> walkOnFluidPredicate, Entity entity, CallbackInfo ci) {
		this.walkOnFluidPredicate = this.walkOnFluidPredicate.or(state -> entity != null && EnchancementUtil.shouldFluidWalk(entity));
	}
}
