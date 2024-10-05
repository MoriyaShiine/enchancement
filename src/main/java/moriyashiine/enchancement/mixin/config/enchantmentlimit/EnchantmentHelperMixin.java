/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.enchantmentlimit;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Unique
	private static ItemEnchantmentsComponent removeUntilReady(ItemStack stack, ItemEnchantmentsComponent itemEnchantmentsComponent) {
		List<RegistryEntry<Enchantment>> enchantments = new ArrayList<>(itemEnchantmentsComponent.getEnchantments());
		while (EnchancementUtil.limitCheck(false, EnchancementUtil.getNonDefaultEnchantmentsSize(stack, enchantments.size()) > ModConfig.enchantmentLimit)) {
			enchantments.removeFirst();
		}
		ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(itemEnchantmentsComponent);
		builder.remove(enchantment -> !enchantments.contains(enchantment));
		return builder.build();
	}

	@WrapOperation(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/ItemEnchantmentsComponent$Builder;build()Lnet/minecraft/component/type/ItemEnchantmentsComponent;"))
	private static ItemEnchantmentsComponent enchancement$enchantmentLimit(ItemEnchantmentsComponent.Builder instance, Operation<ItemEnchantmentsComponent> original, ItemStack stack) {
		return removeUntilReady(stack, original.call(instance));
	}

	@ModifyVariable(method = "set", at = @At("HEAD"), argsOnly = true)
	private static ItemEnchantmentsComponent enchancement$enchantmentLimit(ItemEnchantmentsComponent value, ItemStack stack) {
		return removeUntilReady(stack, value);
	}

	@ModifyReturnValue(method = "generateEnchantments", at = @At(value = "RETURN", ordinal = 1))
	private static List<EnchantmentLevelEntry> enchancement$enchantmentLimit(List<EnchantmentLevelEntry> original, Random random, ItemStack stack) {
		for (int i = original.size() - 1; i >= 0; i--) {
			if (EnchancementUtil.limitCheck(false, EnchancementUtil.getNonDefaultEnchantmentsSize(stack, original.size()) > ModConfig.enchantmentLimit)) {
				original.remove(i);
			}
		}
		return original;
	}
}
