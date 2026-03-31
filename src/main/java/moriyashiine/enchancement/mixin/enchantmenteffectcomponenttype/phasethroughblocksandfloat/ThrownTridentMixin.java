/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.phasethroughblocksandfloat;

import moriyashiine.enchancement.common.component.entity.PhaseThroughBlocksAndFloatComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {
	protected ThrownTridentMixin(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$phaseThroughBlocksAndFloat(Level level, LivingEntity owner, ItemStack tridentItem, CallbackInfo ci) {
		PhaseThroughBlocksAndFloatComponent.maybeSet(owner, tridentItem, this);
	}
}
