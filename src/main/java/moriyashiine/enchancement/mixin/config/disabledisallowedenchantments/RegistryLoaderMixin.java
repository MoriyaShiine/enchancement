/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Decoder;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {
	@SuppressWarnings("unchecked")
	@Inject(method = "loadFromResource(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", at = @At("TAIL"))
	private static <E> void enchancement$disableDisallowedEnchantments(ResourceManager resourceManager, RegistryOps.RegistryInfoGetter infoGetter, MutableRegistry<E> registry, Decoder<E> elementDecoder, Map<RegistryKey<?>, Exception> errors, CallbackInfo ci) {
		if (registry.getKey().equals(RegistryKeys.ENCHANTMENT)) {
			registry.add((RegistryKey<E>) ModEnchantments.EMPTY_KEY, (E) ModEnchantments.EMPTY, RegistryEntryInfo.DEFAULT);
		}
	}

	@WrapOperation(method = "loadFromResource(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceFinder;findResources(Lnet/minecraft/resource/ResourceManager;)Ljava/util/Map;"))
	private static <E> Map<Identifier, Resource> enchancement$disableDisallowedEnchantments(ResourceFinder instance, ResourceManager resourceManager, Operation<Map<Identifier, Resource>> original, ResourceManager resourceManager0, RegistryOps.RegistryInfoGetter infoGetter, MutableRegistry<E> registry, @Local ResourceFinder resourceFinder) {
		Map<Identifier, Resource> originalMap = original.call(instance, resourceManager);
		if (registry.getKey().equals(RegistryKeys.ENCHANTMENT)) {
			Map<Identifier, Resource> map = new HashMap<>();
			originalMap.forEach((key, value) -> {
				if (EnchancementUtil.isEnchantmentAllowed(resourceFinder.toResourceId(key))) {
					map.put(key, value);
				}
			});
			return map;
		}
		return originalMap;
	}
}
