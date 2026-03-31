/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Registry.class)
public interface RegistryMixin<T> {
	@Shadow
	ResourceKey<? extends Registry<T>> key();

	@Shadow
	@Nullable T getValue(@Nullable ResourceKey<T> key);

	@SuppressWarnings("unchecked")
	@WrapOperation(method = "getValueOrThrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;getValue(Lnet/minecraft/resources/ResourceKey;)Ljava/lang/Object;"))
	private @Nullable T enchancement$disableDisallowedEnchantments(Registry<?> instance, @Nullable ResourceKey<T> key, Operation<T> original) {
		T value = original.call(instance, key);
		if (value == null && key().equals(Registries.ENCHANTMENT)) {
			value = getValue((ResourceKey<T>) ModEnchantments.EMPTY_KEY);
		}
		return value;
	}
}
