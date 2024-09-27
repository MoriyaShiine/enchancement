/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryEntryListCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantRandomlyLootFunction.class)
public class EnchantRandomlyLootFunctionMixin {
	@WrapOperation(method = "method_60292", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/RegistryCodecs;entryList(Lnet/minecraft/registry/RegistryKey;)Lcom/mojang/serialization/Codec;"))
	private static Codec<RegistryEntryList<Enchantment>> enchancement$disableDisallowedEnchantments(RegistryKey<? extends Registry<Enchantment>> registryRef, Operation<Codec<RegistryEntryList<Enchantment>>> original) {
		Codec<RegistryEntryList<Enchantment>> codec = RegistryEntryListCodec.create(RegistryKeys.ENCHANTMENT, Enchantment.ENTRY_CODEC, false);
		return new Codec<>() {
			@Override
			public <T> DataResult<Pair<RegistryEntryList<Enchantment>, T>> decode(DynamicOps<T> ops, T input) {
				DataResult<Pair<RegistryEntryList<Enchantment>, T>> dataResult = codec.decode(ops, input);
				if (dataResult.hasResultOrPartial()) {
					return dataResult;
				}
				return DataResult.success(Pair.of(RegistryEntryList.of(), input));
			}

			@Override
			public <T> DataResult<T> encode(RegistryEntryList<Enchantment> input, DynamicOps<T> ops, T prefix) {
				return codec.encode(input, ops, prefix);
			}
		};
	}
}
