/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.singlelevelmode;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = EnchantmentHelper.class, priority = 1001)
public class EnchantmentHelperMixin {
	@WrapOperation(method = "updateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ItemEnchantments$Mutable;toImmutable()Lnet/minecraft/world/item/enchantment/ItemEnchantments;"))
	private static ItemEnchantments enchancement$singleLevelMode(ItemEnchantments.Mutable instance, Operation<ItemEnchantments> original) {
		ItemEnchantments enchantments = original.call(instance);
		if (ModConfig.singleLevelMode) {
			ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
			enchantments.keySet().forEach(enchantment -> mutable.upgrade(enchantment, 1));
			return mutable.toImmutable();
		}
		return enchantments;
	}

	@ModifyVariable(method = "setEnchantments", at = @At("HEAD"), argsOnly = true)
	private static ItemEnchantments enchancement$singleLevelMode(ItemEnchantments enchantments, ItemStack itemStack) {
		if (ModConfig.singleLevelMode) {
			ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(enchantments);
			enchantments.keySet().forEach(enchantment -> mutable.set(enchantment, 1));
			return mutable.toImmutable();
		}
		return enchantments;
	}

	@WrapOperation(method = "runIterationOnItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;accept(Lnet/minecraft/core/Holder;I)V"))
	private static void enchancement$singleLevelMode(EnchantmentHelper.EnchantmentVisitor instance, Holder<Enchantment> enchantmentRegistryEntry, int i, Operation<Void> original, ItemStack piece) {
		if (ModConfig.singleLevelMode) {
			i = EnchancementUtil.alterLevel(piece, enchantmentRegistryEntry);
		}
		original.call(instance, enchantmentRegistryEntry, i);
	}

	@WrapOperation(method = "runIterationOnItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentInSlotVisitor;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentInSlotVisitor;accept(Lnet/minecraft/core/Holder;ILnet/minecraft/world/item/enchantment/EnchantedItemInUse;)V"))
	private static void enchancement$singleLevelMode(EnchantmentHelper.EnchantmentInSlotVisitor instance, Holder<Enchantment> enchantmentRegistryEntry, int i, EnchantedItemInUse enchantmentEffectContext, Operation<Void> original, ItemStack piece) {
		if (ModConfig.singleLevelMode) {
			i = EnchancementUtil.alterLevel(piece, enchantmentRegistryEntry);
		}
		original.call(instance, enchantmentRegistryEntry, i, enchantmentEffectContext);
	}

	@ModifyReturnValue(method = "getItemEnchantmentLevel", at = @At("RETURN"))
	private static int enchancement$singleLevelMode(int original, Holder<Enchantment> enchantment, ItemInstance piece) {
		if (original > 0 && ModConfig.singleLevelMode) {
			return EnchancementUtil.alterLevel(piece, enchantment);
		}
		return original;
	}
}
