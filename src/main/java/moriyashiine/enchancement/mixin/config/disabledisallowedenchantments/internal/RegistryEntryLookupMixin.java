/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(RegistryEntryLookup.class)
public interface RegistryEntryLookupMixin<T> {
	@Shadow
	RegistryEntry.Reference<T> getOrThrow(RegistryKey<T> key);

	@SuppressWarnings("unchecked")
	@WrapOperation(method = "getOrThrow(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/registry/entry/RegistryEntry$Reference;", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/RegistryEntryLookup;getOptional(Lnet/minecraft/registry/RegistryKey;)Ljava/util/Optional;"))
	private Optional<RegistryEntry.Reference<T>> enchancement$disableDisallowedEnchantments(RegistryEntryLookup<T> instance, RegistryKey<T> key, Operation<Optional<RegistryEntry.Reference<T>>> original) {
		if ((Object) this instanceof Registry<?> registry && registry.getKey().equals(RegistryKeys.ENCHANTMENT) && key != ModEnchantments.EMPTY_KEY && !EnchancementUtil.isEnchantmentAllowed(key.getValue())) {
			key = (RegistryKey<T>) ModEnchantments.EMPTY_KEY;
		}
		return original.call(instance, key);
	}
}
