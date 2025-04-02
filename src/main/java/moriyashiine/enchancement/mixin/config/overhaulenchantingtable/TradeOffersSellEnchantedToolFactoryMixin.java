/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchantingtable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.OverhaulMode;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(TradeOffers.SellEnchantedToolFactory.class)
public class TradeOffersSellEnchantedToolFactoryMixin {
	@WrapOperation(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;enchant(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/item/ItemStack;ILnet/minecraft/registry/DynamicRegistryManager;Ljava/util/Optional;)Lnet/minecraft/item/ItemStack;"))
	private ItemStack enchancement$overhaulEnchantingTable(Random random, ItemStack stack, int level, DynamicRegistryManager dynamicRegistryManager, Optional<? extends RegistryEntryList<Enchantment>> enchantments, Operation<ItemStack> original) {
		if (ModConfig.overhaulEnchantingTable == OverhaulMode.CHISELED) {
			return stack;
		}
		return original.call(random, stack, level, dynamicRegistryManager, enchantments);
	}
}
