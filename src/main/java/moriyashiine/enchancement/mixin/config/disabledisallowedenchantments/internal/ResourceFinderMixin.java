/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;

@Mixin(ResourceFinder.class)
public abstract class ResourceFinderMixin {
	@Shadow
	public abstract Identifier toResourceId(Identifier path);

	@ModifyReturnValue(method = "findResources", at = @At("RETURN"))
	private Map<Identifier, Resource> enchancement$disableDisallowedEnchantments(Map<Identifier, Resource> original) {
		Map<Identifier, Resource> map = new HashMap<>();
		original.forEach((key, value) -> {
			if (!key.getPath().startsWith("enchantment/") || EnchancementUtil.isEnchantmentAllowed(toResourceId(key)) || toResourceId(key).equals(ModEnchantments.EMPTY_KEY.getValue())) {
				map.put(key, value);
			}
		});
		return map;
	}
}
