/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.ingame;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "apply", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
	private static void enchancement$disableDisallowedEnchantments(ItemStack stack, Consumer<ItemEnchantmentsComponent.Builder> applier, CallbackInfoReturnable<ItemEnchantmentsComponent> cir) {
		EnchancementUtil.cachedApplyStack = stack;
	}

	@Inject(method = "apply", at = @At(value = "RETURN", ordinal = 1))
	private static void enchancement$disableDisallowedEnchantmentsReturn(ItemStack stack, Consumer<ItemEnchantmentsComponent.Builder> applier, CallbackInfoReturnable<ItemEnchantmentsComponent> cir) {
		EnchancementUtil.cachedApplyStack = null;
	}

	@ModifyVariable(method = "set", at = @At("HEAD"), argsOnly = true)
	private static ItemEnchantmentsComponent enchancement$disableDisallowedEnchantments(ItemEnchantmentsComponent value, ItemStack stack) {
		ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
		value.getEnchantments().forEach(enchantment -> {
			int level = value.getLevel(enchantment);
			if (EnchancementUtil.isEnchantmentAllowed(enchantment)) {
				builder.add(enchantment, level);
			} else {
				@Nullable RegistryEntry<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(stack, EnchantmentTags.ON_RANDOM_LOOT, null);
				if (randomEnchantment != null) {
					builder.add(randomEnchantment, Math.min(level, randomEnchantment.value().getMaxLevel()));
				}
			}
		});
		return builder.build();
	}

	@WrapOperation(method = "generateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getPossibleEntries(ILnet/minecraft/item/ItemStack;Ljava/util/stream/Stream;)Ljava/util/List;"))
	private static List<EnchantmentLevelEntry> enchancement$disableDisallowedEnchantments(int level, ItemStack stack, Stream<RegistryEntry<Enchantment>> possibleEnchantments, Operation<List<EnchantmentLevelEntry>> original, Random random) {
		List<EnchantmentLevelEntry> enchantments = original.call(level, stack, possibleEnchantments);
		for (int i = enchantments.size() - 1; i >= 0; i--) {
			RegistryEntry<Enchantment> enchantment = enchantments.get(i).enchantment();
			if (!EnchancementUtil.isEnchantmentAllowed(enchantment)) {
				@Nullable RegistryEntry<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(stack, EnchantmentTags.ON_RANDOM_LOOT, random);
				if (randomEnchantment == null) {
					enchantments.remove(i);
				} else {
					if (enchantments.stream().anyMatch(entry -> entry.enchantment().equals(randomEnchantment))) {
						enchantments.remove(i);
					} else {
						enchantments.set(i, new EnchantmentLevelEntry(randomEnchantment, 1));
					}
				}
			}
		}
		if (enchantments.isEmpty()) {
			@Nullable RegistryEntry<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(stack, EnchantmentTags.IN_ENCHANTING_TABLE, random);
			if (randomEnchantment != null) {
				enchantments.add(new EnchantmentLevelEntry(randomEnchantment, 1));
			}
		}
		return enchantments;
	}
}
