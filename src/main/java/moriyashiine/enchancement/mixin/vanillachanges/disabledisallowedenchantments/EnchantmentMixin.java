/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Shadow
	@Final
	@Mutable
	public static Codec<RegistryEntry<Enchantment>> ENTRY_CODEC;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void enchancement$disableDisallowedEnchantments(CallbackInfo ci) {
		ENTRY_CODEC = new Codec<>() {
			static final Codec<RegistryEntry<Enchantment>> BASE = RegistryFixedCodec.of(RegistryKeys.ENCHANTMENT);

			@Override
			public <T> DataResult<Pair<RegistryEntry<Enchantment>, T>> decode(DynamicOps<T> ops, T input) {
				DataResult<Pair<RegistryEntry<Enchantment>, T>> dataResult = BASE.decode(ops, input);
				if (dataResult.hasResultOrPartial()) {
					return dataResult;
				}
				if (ops instanceof RegistryOps<T> registryOps) {
					DataResult<Pair<Identifier, T>> idResult = Identifier.CODEC.decode(ops, input);
					if (idResult.hasResultOrPartial()) {
						Identifier id = idResult.resultOrPartial().get().getFirst();
						if (!EnchancementUtil.isEnchantmentAllowed(id)) {
							return registryOps.getEntryLookup(RegistryKeys.ENCHANTMENT).flatMap(lookup -> lookup.getOptional(Enchantments.SILK_TOUCH))
									.map(enchantment -> DataResult.success(Pair.of((RegistryEntry<Enchantment>) enchantment, input))).orElseThrow();
						}
					}
				}
				return dataResult;
			}

			@Override
			public <T> DataResult<T> encode(RegistryEntry<Enchantment> input, DynamicOps<T> ops, T prefix) {
				return BASE.encode(input, ops, prefix);
			}
		};
	}
}
