/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.init;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(Registry.class)
public interface RegistryMixin<T> {
	@Shadow
	RegistryKey<? extends Registry<T>> getKey();

	@Shadow
	Optional<RegistryEntry.Reference<T>> getEntry(RegistryKey<T> key);

	@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unchecked"})
	@ModifyExpressionValue(method = "entryOf", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;getEntry(Lnet/minecraft/registry/RegistryKey;)Ljava/util/Optional;"))
	private Optional<RegistryEntry.Reference<T>> enchancement$disableDisallowedEnchantments(Optional<RegistryEntry.Reference<T>> original) {
		if (original.isEmpty() && getKey().equals(RegistryKeys.ENCHANTMENT)) {
			return getEntry((RegistryKey<T>) ModEnchantments.EMPTY_KEY);
		}
		return original;
	}
}
