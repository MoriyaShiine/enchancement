/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.provider.SingleEnchantmentProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SingleEnchantmentProvider.class)
public class SingleEnchantmentProviderMixin {
	@WrapOperation(method = "provideEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/ItemEnchantmentsComponent$Builder;add(Lnet/minecraft/registry/entry/RegistryEntry;I)V"))
	private void enchancement$disableDisallowedEnchantments(ItemEnchantmentsComponent.Builder instance, RegistryEntry<Enchantment> enchantment, int level, Operation<Void> original, ItemStack stack) {
		if (!EnchancementUtil.isEnchantmentAllowed(enchantment)) {
			enchantment = EnchancementUtil.getReplacement(enchantment, stack);
			if (enchantment == null) {
				return;
			}
			level = Math.min(enchantment.value().getMaxLevel(), level);
		}
		original.call(instance, enchantment, level);
	}
}
