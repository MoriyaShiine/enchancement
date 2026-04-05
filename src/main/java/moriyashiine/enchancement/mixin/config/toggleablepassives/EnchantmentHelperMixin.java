/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.toggleablepassives;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import moriyashiine.enchancement.common.tag.ModItemTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Unique
	private static void checkPassive(ItemStack stack, ItemEnchantments enchantments) {
		if (ModConfig.toggleablePassives && isApplicable(stack)) {
			if (stack.isEnchanted()) {
				if (!stack.has(ModComponentTypes.TOGGLEABLE_PASSIVE)) {
					for (Holder<Enchantment> enchantment : enchantments.keySet()) {
						if (enchantment.is(ModEnchantmentTags.DISALLOWS_TOGGLEABLE_PASSIVE)) {
							return;
						}
					}
					stack.set(ModComponentTypes.TOGGLEABLE_PASSIVE, true);
				}
			} else {
				stack.remove(ModComponentTypes.TOGGLEABLE_PASSIVE);
			}
		}
	}

	@Unique
	private static boolean isApplicable(ItemStack stack) {
		return EnchancementUtil.isGroundAnimalArmor(stack) || stack.is(ItemTags.CHEST_ARMOR_ENCHANTABLE) || stack.is(ItemTags.MINING_ENCHANTABLE) || stack.is(ItemTags.TRIDENT_ENCHANTABLE);
	}

	@Inject(method = "updateEnchantments", at = @At(value = "RETURN", ordinal = 1))
	private static void enchancement$toggleablePassives(ItemStack itemStack, Consumer<ItemEnchantments.Mutable> consumer, CallbackInfoReturnable<ItemEnchantments> cir, @Local(name = "newEnchantments") ItemEnchantments newEnchantments) {
		checkPassive(itemStack, newEnchantments);
	}

	@Inject(method = "setEnchantments", at = @At("TAIL"))
	private static void enchancement$toggleablePassives(ItemStack itemStack, ItemEnchantments enchantments, CallbackInfo ci) {
		checkPassive(itemStack, enchantments);
	}

	@ModifyReturnValue(method = "getTridentReturnToOwnerAcceleration", at = @At("RETURN"))
	private static int enchancement$toggleablePassives(int original, ServerLevel serverLevel, ItemStack weapon) {
		if (ModConfig.toggleablePassives && weapon.is(ItemTags.TRIDENT_ENCHANTABLE) && !weapon.is(ModItemTags.NO_LOYALTY) && weapon.getOrDefault(ModComponentTypes.TOGGLEABLE_PASSIVE, false)) {
			if (!weapon.isEnchanted()) {
				weapon.remove(ModComponentTypes.TOGGLEABLE_PASSIVE);
				return original;
			}
			return EnchancementUtil.hasWeakEnchantments(weapon) ? 1 : 3;
		}
		return original;
	}
}
