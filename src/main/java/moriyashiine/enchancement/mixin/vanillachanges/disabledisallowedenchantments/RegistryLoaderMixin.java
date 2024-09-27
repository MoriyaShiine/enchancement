/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryOps;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;

@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {
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
