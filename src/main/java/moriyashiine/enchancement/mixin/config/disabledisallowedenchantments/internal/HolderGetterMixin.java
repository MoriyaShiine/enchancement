/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(HolderGetter.class)
public interface HolderGetterMixin<T> {
	@SuppressWarnings("unchecked")
	@WrapOperation(method = "getOrThrow(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/core/Holder$Reference;", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/HolderGetter;get(Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;"))
	private Optional<Holder.Reference<T>> enchancement$disableDisallowedEnchantments(HolderGetter<T> instance, ResourceKey<T> key, Operation<Optional<Holder.Reference<T>>> original) {
		if ((Object) this instanceof Registry<?> registry && registry.key().equals(Registries.ENCHANTMENT) && key != ModEnchantments.EMPTY_KEY && !EnchancementUtil.isEnchantmentAllowed(key.identifier())) {
			key = (ResourceKey<T>) ModEnchantments.EMPTY_KEY;
		}
		return original.call(instance, key);
	}
}
