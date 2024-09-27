/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.singlelevelmode;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EnchantmentHelper.class, priority = 1001)
public class EnchantmentHelperMixin {
	@WrapOperation(method = "forEachEnchantment(Lnet/minecraft/item/ItemStack;Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;accept(Lnet/minecraft/registry/entry/RegistryEntry;I)V"))
	private static void enchancement$singleLevelMode(EnchantmentHelper.Consumer instance, RegistryEntry<Enchantment> enchantmentRegistryEntry, int i, Operation<Void> original, ItemStack stack) {
		if (ModConfig.singleLevelMode) {
			i = EnchancementUtil.alterLevel(stack, enchantmentRegistryEntry);
		}
		original.call(instance, enchantmentRegistryEntry, i);
	}

	@WrapOperation(method = "forEachEnchantment(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/enchantment/EnchantmentHelper$ContextAwareConsumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper$ContextAwareConsumer;accept(Lnet/minecraft/registry/entry/RegistryEntry;ILnet/minecraft/enchantment/EnchantmentEffectContext;)V"))
	private static void enchancement$singleLevelMode(EnchantmentHelper.ContextAwareConsumer instance, RegistryEntry<Enchantment> enchantmentRegistryEntry, int i, EnchantmentEffectContext enchantmentEffectContext, Operation<Void> original, ItemStack stack) {
		if (ModConfig.singleLevelMode) {
			i = EnchancementUtil.alterLevel(stack, enchantmentRegistryEntry);
		}
		original.call(instance, enchantmentRegistryEntry, i, enchantmentEffectContext);
	}

	@ModifyReturnValue(method = "getLevel", at = @At("RETURN"))
	private static int enchancement$singleLevelMode(int original, RegistryEntry<Enchantment> enchantment, ItemStack stack) {
		if (original > 0 && ModConfig.singleLevelMode) {
			return EnchancementUtil.alterLevel(stack, enchantment);
		}
		return original;
	}
}
