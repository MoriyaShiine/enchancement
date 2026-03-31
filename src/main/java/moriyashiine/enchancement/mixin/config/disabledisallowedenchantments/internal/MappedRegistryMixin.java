/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Lifecycle;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> {
	@Shadow
	public abstract int getId(@Nullable T thing);

	@Shadow
	public abstract @Nullable T getValue(@Nullable ResourceKey<T> key);

	@Shadow
	public abstract Optional<ResourceKey<T>> getResourceKey(T thing);

	@Shadow
	@Final
	private ResourceKey<? extends Registry<T>> key;

	@Inject(method = "<init>(Lnet/minecraft/resources/ResourceKey;Lcom/mojang/serialization/Lifecycle;Z)V", at = @At("TAIL"))
	private void enchancement$disableDisallowedEnchantments(ResourceKey<?> key, Lifecycle initialLifecycle, boolean intrusiveHolders, CallbackInfo ci) {
		if (key.equals(Registries.ENCHANTMENT)) {
			EnchancementUtil.ENCHANTMENT_HOLDER_OWNER = (HolderOwner<?>) this;
		}
	}

	@SuppressWarnings("unchecked")
	@ModifyReturnValue(method = "getId", at = @At("RETURN"))
	private int enchancement$disableDisallowedEnchantments(int original) {
		if (original == -1 && key.equals(Registries.ENCHANTMENT)) {
			return getId(getValue((ResourceKey<T>) ModEnchantments.EMPTY_KEY));
		}
		return original;
	}

	@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unchecked"})
	@ModifyReturnValue(method = "getResourceKey(Ljava/lang/Object;)Ljava/util/Optional;", at = @At("RETURN"))
	private Optional<ResourceKey<T>> enchancement$disableDisallowedEnchantments(Optional<ResourceKey<T>> original) {
		if (original.isEmpty() && key.equals(Registries.ENCHANTMENT)) {
			return getResourceKey(getValue((ResourceKey<T>) ModEnchantments.EMPTY_KEY));
		}
		return original;
	}
}
