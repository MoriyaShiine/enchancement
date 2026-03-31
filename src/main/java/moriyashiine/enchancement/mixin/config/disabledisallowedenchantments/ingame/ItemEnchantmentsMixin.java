/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.ingame;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemEnchantments.class)
public class ItemEnchantmentsMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Object2IntOpenHashMap<Holder<Enchantment>> enchancement$disableDisallowedEnchantments(Object2IntOpenHashMap<Holder<Enchantment>> enchantments) {
		Object2IntOpenHashMap<Holder<Enchantment>> newEnchantments = new Object2IntOpenHashMap<>();
		enchantments.forEach((entry, level) -> {
			if (!entry.is(ModEnchantments.EMPTY_KEY)) {
				newEnchantments.put(entry, level);
			}
		});
		return newEnchantments;
	}
}
