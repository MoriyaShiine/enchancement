/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;

@Mixin(FileToIdConverter.class)
public abstract class FileToIdConverterMixin {
	@Shadow
	public abstract Identifier fileToId(Identifier file);

	@ModifyReturnValue(method = "listMatchingResources", at = @At("RETURN"))
	private Map<Identifier, Resource> enchancement$disableDisallowedEnchantments(Map<Identifier, Resource> original) {
		Map<Identifier, Resource> newMap = new HashMap<>();
		original.forEach((key, value) -> {
			if (!key.getPath().startsWith("enchantment/") || EnchancementUtil.isEnchantmentAllowed(fileToId(key)) || fileToId(key).equals(ModEnchantments.EMPTY_KEY.identifier())) {
				newMap.put(key, value);
			}
		});
		return newMap;
	}
}
