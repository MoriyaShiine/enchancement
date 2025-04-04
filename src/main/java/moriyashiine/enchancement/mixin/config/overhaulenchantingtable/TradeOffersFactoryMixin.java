/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchantingtable;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.village.TradeOffers;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(value = {TradeOffers.SellItemFactory.class, TradeOffers.ProcessItemFactory.class})
public class TradeOffersFactoryMixin {
	@WrapWithCondition(method = "create", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"))
	private <T> boolean enchancement$overhaulEnchantingTable(Optional<?> instance, Consumer<? super @NotNull T> action) {
		return !ModConfig.overhaulEnchantingTable.chiseledMode();
	}
}
