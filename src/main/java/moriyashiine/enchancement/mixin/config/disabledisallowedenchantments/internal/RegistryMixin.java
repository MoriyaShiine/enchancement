/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Registry.class)
public interface RegistryMixin<T> {
	@Shadow
	RegistryKey<? extends Registry<T>> getKey();

	@Shadow
	@Nullable T get(@Nullable RegistryKey<T> key);

	@SuppressWarnings("unchecked")
	@WrapOperation(method = "getValueOrThrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;get(Lnet/minecraft/registry/RegistryKey;)Ljava/lang/Object;"))
	private @Nullable T enchancement$disableDisallowedEnchantments(Registry<?> instance, @Nullable RegistryKey<T> key, Operation<T> original) {
		T value = original.call(instance, key);
		if (value == null && getKey().equals(RegistryKeys.ENCHANTMENT)) {
			value = get((RegistryKey<T>) ModEnchantments.EMPTY_KEY);
		}
		return value;
	}
}
