/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.codec;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryEntryListCodec;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(RegistryEntryListCodec.class)
public class RegistryEntryListCodecMixin<E> {
	@Shadow
	@Final
	private RegistryKey<? extends Registry<E>> registry;

	@SuppressWarnings("unchecked")
	@ModifyReturnValue(method = "decode", at = @At("RETURN"))
	private <T> DataResult<Pair<RegistryEntryList<E>, T>> enchancement$disableDisallowedEnchantments(DataResult<Pair<RegistryEntryList<E>, T>> original, DynamicOps<T> ops, T input) {
		if (original.hasResultOrPartial() && registry.equals(RegistryKeys.ENCHANTMENT)) {
			List<RegistryEntry<E>> entries = new ArrayList<>();
			original.getOrThrow().getFirst().forEach(entry -> {
				if (!entry.matchesKey((RegistryKey<E>) ModEnchantments.EMPTY_KEY)) {
					entries.add(entry);
				}
			});
			return DataResult.success(Pair.of(RegistryEntryList.of(entries), input));
		}
		return original;
	}
}
