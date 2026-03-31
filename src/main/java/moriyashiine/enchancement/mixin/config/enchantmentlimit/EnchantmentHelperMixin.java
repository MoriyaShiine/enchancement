/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enchantmentlimit;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Unique
	private static ItemEnchantments removeUntilReady(ItemStack stack, ItemEnchantments itemEnchantmentsComponent) {
		List<Holder<Enchantment>> enchantments = new ArrayList<>(itemEnchantmentsComponent.keySet());
		while (EnchancementUtil.exceedsLimit(stack, enchantments.size())) {
			enchantments.removeFirst();
		}
		ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(itemEnchantmentsComponent);
		mutable.removeIf(enchantment -> !enchantments.contains(enchantment));
		return mutable.toImmutable();
	}

	@WrapOperation(method = "updateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ItemEnchantments$Mutable;toImmutable()Lnet/minecraft/world/item/enchantment/ItemEnchantments;"))
	private static ItemEnchantments enchancement$enchantmentLimit(ItemEnchantments.Mutable instance, Operation<ItemEnchantments> original, ItemStack itemStack) {
		return removeUntilReady(itemStack, original.call(instance));
	}

	@ModifyVariable(method = "setEnchantments", at = @At("HEAD"), argsOnly = true)
	private static ItemEnchantments enchancement$enchantmentLimit(ItemEnchantments enchantments, ItemStack itemStack) {
		return removeUntilReady(itemStack, enchantments);
	}

	@ModifyReturnValue(method = "selectEnchantment", at = @At(value = "RETURN", ordinal = 1))
	private static List<EnchantmentInstance> enchancement$enchantmentLimit(List<EnchantmentInstance> original, RandomSource random, ItemStack itemStack) {
		while (EnchancementUtil.exceedsLimit(itemStack, original.size())) {
			original.removeFirst();
		}
		return original;
	}

	@ModifyExpressionValue(method = "lambda$getAvailableEnchantmentResults$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMinCost(I)I"))
	private static int enchancement$enchantmentLimitMin(int original) {
		if (ModConfig.enchantmentLimit > 0) {
			return 0;
		}
		return original;
	}

	@ModifyExpressionValue(method = "lambda$getAvailableEnchantmentResults$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxCost(I)I"))
	private static int enchancement$enchantmentLimitMax(int original) {
		if (ModConfig.enchantmentLimit > 0) {
			return Integer.MAX_VALUE;
		}
		return original;
	}
}
