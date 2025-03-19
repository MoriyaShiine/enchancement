/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledurability;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin {
	@ModifyExpressionValue(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isDamageable()Z"))
	private boolean enchancement$disableDurability(boolean original, @Local ItemStack stack) {
		return original || (stack.contains(DataComponentTypes.DAMAGE) && stack.contains(DataComponentTypes.MAX_DAMAGE));
	}
}
