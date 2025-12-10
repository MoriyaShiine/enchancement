/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.ingame;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemEnchantmentsComponent.class)
public class ItemEnchantmentsComponentMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Object2IntOpenHashMap<RegistryEntry<Enchantment>> enchancement$disableDisallowedEnchantments(Object2IntOpenHashMap<RegistryEntry<Enchantment>> value) {
		Object2IntOpenHashMap<RegistryEntry<Enchantment>> newMap = new Object2IntOpenHashMap<>();
		value.forEach((entry, level) -> {
			if (!entry.matchesKey(ModEnchantments.EMPTY_KEY)) {
				newMap.put(entry, level);
			}
		});
		return newMap;
	}
}
