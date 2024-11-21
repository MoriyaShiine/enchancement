/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(ItemGroups.class)
public class ItemGroupsMixin {
	@ModifyExpressionValue(method = {"addAllLevelEnchantedBooks", "addMaxLevelEnchantedBooks"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/RegistryWrapper;streamEntries()Ljava/util/stream/Stream;"))
	private static Stream<RegistryEntry.Reference<Enchantment>> enchancement$disableDisallowedEnchantments(Stream<RegistryEntry.Reference<Enchantment>> original) {
		return original.filter(ref -> !ref.matchesKey(ModEnchantments.EMPTY_KEY));
	}
}
