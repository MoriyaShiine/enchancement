/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Unique
	private static ItemEnchantmentsComponent replaceDisallowedEnchantments(ItemStack stack, ItemEnchantmentsComponent itemEnchantmentsComponent) {
		ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
		itemEnchantmentsComponent.getEnchantments().forEach(enchantment -> {
			int level = itemEnchantmentsComponent.getLevel(enchantment);
			if (EnchancementUtil.isEnchantmentAllowed(enchantment)) {
				builder.add(enchantment, level);
			} else {
				@Nullable RegistryEntry<Enchantment> replacement = EnchancementUtil.getReplacement(enchantment, stack);
				if (replacement != null) {
					builder.add(replacement, Math.min(level, replacement.value().getMaxLevel()));
				}
			}
		});
		return builder.build();
	}

	@WrapOperation(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/ItemEnchantmentsComponent$Builder;build()Lnet/minecraft/component/type/ItemEnchantmentsComponent;"))
	private static ItemEnchantmentsComponent enchancement$disableDisallowedEnchantments(ItemEnchantmentsComponent.Builder instance, Operation<ItemEnchantmentsComponent> original, ItemStack stack) {
		return replaceDisallowedEnchantments(stack, original.call(instance));
	}

	@ModifyVariable(method = "set", at = @At("HEAD"), argsOnly = true)
	private static ItemEnchantmentsComponent enchancement$disableDisallowedEnchantments(ItemEnchantmentsComponent value, ItemStack stack) {
		return replaceDisallowedEnchantments(stack, value);
	}

	@ModifyExpressionValue(method = "enchant(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/item/ItemStack;ILjava/util/stream/Stream;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;generateEnchantments(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/item/ItemStack;ILjava/util/stream/Stream;)Ljava/util/List;"))
	private static List<EnchantmentLevelEntry> enchancement$disableDisallowedEnchantments(List<EnchantmentLevelEntry> original, Random random, ItemStack stack) {
		if (original.isEmpty()) {
			@Nullable RegistryEntry<Enchantment> entry = EnchancementUtil.getRandomEnchantment(stack, random);
			if (entry != null) {
				return List.of(new EnchantmentLevelEntry(entry, 1));
			}
		}
		return original;
	}
}
