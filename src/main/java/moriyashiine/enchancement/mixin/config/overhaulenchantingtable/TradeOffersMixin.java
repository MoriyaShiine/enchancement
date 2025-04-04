/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchantingtable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.registry.RegistryKey;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(TradeOffers.class)
public class TradeOffersMixin {
	@Shadow
	@Final
	public static Map<RegistryKey<VillagerProfession>, Int2ObjectMap<TradeOffers.Factory[]>> PROFESSION_TO_LEVELED_TRADE;

	@Shadow
	@Final
	public static Map<RegistryKey<VillagerProfession>, Int2ObjectMap<TradeOffers.Factory[]>> REBALANCED_PROFESSION_TO_LEVELED_TRADE;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void enchancement$overhaulEnchantingTable(CallbackInfo ci) {
		if (ModConfig.overhaulEnchantingTable.chiseledMode()) {
			PROFESSION_TO_LEVELED_TRADE.put(VillagerProfession.LIBRARIAN, REBALANCED_PROFESSION_TO_LEVELED_TRADE.get(VillagerProfession.LIBRARIAN));
		}
	}
}
