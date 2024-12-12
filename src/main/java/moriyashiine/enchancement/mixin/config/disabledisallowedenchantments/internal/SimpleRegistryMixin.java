/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Lifecycle;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntryOwner;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> {
	@Shadow
	public abstract int getRawId(@Nullable T value);

	@Shadow
	public abstract @Nullable T get(@Nullable RegistryKey<T> key);

	@Shadow
	@Final
	private RegistryKey<? extends Registry<T>> key;

	@Inject(method = "<init>(Lnet/minecraft/registry/RegistryKey;Lcom/mojang/serialization/Lifecycle;Z)V", at = @At("TAIL"))
	private void enchancement$disableDisallowedEnchantments(RegistryKey<?> key, Lifecycle lifecycle, boolean intrusive, CallbackInfo ci) {
		if (key.equals(RegistryKeys.ENCHANTMENT)) {
			EnchancementUtil.ENCHANTMENT_REGISTRY_OWNER = (RegistryEntryOwner<?>) this;
		}
	}

	@SuppressWarnings("unchecked")
	@ModifyReturnValue(method = "getRawId", at = @At("RETURN"))
	private int enchancement$disableDisallowedEnchantments(int original) {
		if (original == -1 && key.equals(RegistryKeys.ENCHANTMENT)) {
			return getRawId(get((RegistryKey<T>) ModEnchantments.EMPTY_KEY));
		}
		return original;
	}
}
