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
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RegistryFixedCodec.class)
public class RegistryFixedCodecMixin<E> {
	@Shadow
	@Final
	private RegistryKey<? extends Registry<E>> registry;

	@SuppressWarnings("unchecked")
	@ModifyReturnValue(method = "decode", at = @At("RETURN"))
	private <T> DataResult<Pair<RegistryEntry<E>, T>> enchancement$disableDisallowedEnchantments(DataResult<Pair<RegistryEntry<E>, T>> original, DynamicOps<T> ops, T input) {
		if (!original.hasResultOrPartial() && registry.equals(RegistryKeys.ENCHANTMENT) && ops instanceof RegistryOps<T> registryOps) {
			return registryOps
					.getEntryLookup(registry)
					.flatMap(lookup -> lookup.getOptional((RegistryKey<E>) ModEnchantments.EMPTY_KEY))
					.map(entry -> DataResult.success(Pair.of((RegistryEntry<E>) entry, input)))
					.orElseThrow();
		}
		return original;
	}
}
