/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.trading.VillagerTrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(VillagerTrade.class)
public class VillagerTradeMixin {
	@ModifyArg(method = "getOffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/trading/MerchantOffer;<init>(Lnet/minecraft/world/item/trading/ItemCost;Ljava/util/Optional;Lnet/minecraft/world/item/ItemStack;IIF)V"))
	private ItemStack enchancement$overhaulEnchanting(ItemStack result) {
		if (ModConfig.overhaulEnchanting != OverhaulMode.DISABLED) {
			result.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
		}
		return result;
	}
}
