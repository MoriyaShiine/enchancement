/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.applyrandomstatuseffect;

import moriyashiine.enchancement.common.component.entity.ApplyRandomStatusEffectComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
	@Inject(method = "asItemStack", at = @At("HEAD"), cancellable = true)
	private void enchancement$applyRandomStatusEffect(CallbackInfoReturnable<ItemStack> cir) {
		ApplyRandomStatusEffectComponent applyRandomStatusEffectComponent = ModEntityComponents.APPLY_RANDOM_STATUS_EFFECT.getNullable(this);
		if (applyRandomStatusEffectComponent != null && !applyRandomStatusEffectComponent.getOriginalStack().isEmpty()) {
			cir.setReturnValue(applyRandomStatusEffectComponent.getOriginalStack());
		}
	}
}
