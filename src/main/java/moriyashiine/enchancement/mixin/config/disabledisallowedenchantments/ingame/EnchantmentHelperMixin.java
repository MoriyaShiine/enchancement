/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.ingame;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jspecify.annotations.Nullable;
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
	@Inject(method = "updateEnchantments", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
	private static void enchancement$disableDisallowedEnchantments(ItemStack itemStack, Consumer<ItemEnchantments.Mutable> consumer, CallbackInfoReturnable<ItemEnchantments> cir) {
		EnchancementUtil.cachedApplyStack = itemStack;
	}

	@Inject(method = "updateEnchantments", at = @At(value = "RETURN", ordinal = 1))
	private static void enchancement$disableDisallowedEnchantmentsReturn(ItemStack itemStack, Consumer<ItemEnchantments.Mutable> consumer, CallbackInfoReturnable<ItemEnchantments> cir) {
		EnchancementUtil.cachedApplyStack = null;
	}

	@ModifyVariable(method = "setEnchantments", at = @At("HEAD"), argsOnly = true)
	private static ItemEnchantments enchancement$disableDisallowedEnchantments(ItemEnchantments enchantments, ItemStack itemStack) {
		ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		enchantments.keySet().forEach(enchantment -> {
			int level = enchantments.getLevel(enchantment);
			if (EnchancementUtil.isEnchantmentAllowed(enchantment)) {
				mutable.upgrade(enchantment, level);
			} else {
				@Nullable Holder<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(itemStack, EnchantmentTags.ON_RANDOM_LOOT, null);
				if (randomEnchantment != null) {
					mutable.upgrade(randomEnchantment, Math.min(level, randomEnchantment.value().getMaxLevel()));
				}
			}
		});
		return mutable.toImmutable();
	}

	@WrapOperation(method = "selectEnchantment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getAvailableEnchantmentResults(ILnet/minecraft/world/item/ItemStack;Ljava/util/stream/Stream;)Ljava/util/List;"))
	private static List<EnchantmentInstance> enchancement$disableDisallowedEnchantments(int value, ItemStack itemStack, Stream<Holder<Enchantment>> source, Operation<List<EnchantmentInstance>> original, RandomSource random) {
		List<EnchantmentInstance> enchantments = original.call(value, itemStack, source);
		for (int i = enchantments.size() - 1; i >= 0; i--) {
			Holder<Enchantment> enchantment = enchantments.get(i).enchantment();
			if (!EnchancementUtil.isEnchantmentAllowed(enchantment)) {
				@Nullable Holder<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(itemStack, EnchantmentTags.ON_RANDOM_LOOT, random);
				if (randomEnchantment == null) {
					enchantments.remove(i);
				} else {
					if (enchantments.stream().anyMatch(entry -> entry.enchantment().equals(randomEnchantment))) {
						enchantments.remove(i);
					} else {
						enchantments.set(i, new EnchantmentInstance(randomEnchantment, 1));
					}
				}
			}
		}
		if (enchantments.isEmpty()) {
			@Nullable Holder<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(itemStack, EnchantmentTags.IN_ENCHANTING_TABLE, random);
			if (randomEnchantment != null) {
				enchantments.add(new EnchantmentInstance(randomEnchantment, 1));
			}
		}
		return enchantments;
	}
}
