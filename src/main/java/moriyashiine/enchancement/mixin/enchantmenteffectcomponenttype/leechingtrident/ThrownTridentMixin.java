/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.leechingtrident;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.LeechingTridentComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {
	protected ThrownTridentMixin(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$leechingTrident(Level level, LivingEntity owner, ItemStack tridentItem, CallbackInfo ci) {
		LeechingTridentComponent.maybeSet(owner, tridentItem, this);
	}

	@ModifyVariable(method = "tick", at = @At("STORE"), name = "loyalty")
	private int enchancement$leechingTrident(int loyalty) {
		if (ModEntityComponents.LEECHING_TRIDENT.get(this).getStuckEntity() != null) {
			return 0;
		}
		return loyalty;
	}
}
