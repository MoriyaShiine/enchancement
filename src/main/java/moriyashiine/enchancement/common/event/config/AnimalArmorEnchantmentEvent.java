/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.config;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EnchantableComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public class AnimalArmorEnchantmentEvent {
	public static class AllowComponent implements DefaultItemComponentEvents.ModifyCallback {
		@Override
		public void modify(DefaultItemComponentEvents.ModifyContext context) {
			if (ModConfig.rebalanceEquipment) {
				context.modify(item -> EnchancementUtil.isBodyArmor(item.getDefaultStack()), (builder, item) -> builder.add(DataComponentTypes.ENCHANTABLE, new EnchantableComponent(1)));
			}
		}
	}

	public static class AllowEnchanting implements EnchantmentEvents.AllowEnchanting {
		@Override
		public TriState allowEnchanting(RegistryEntry<Enchantment> enchantment, ItemStack target, EnchantingContext enchantingContext) {
			if (ModConfig.rebalanceEquipment && EnchancementUtil.isBodyArmor(target) && enchantment.isIn(ModEnchantmentTags.ANIMAL_ARMOR_ENCHANTMENTS)) {
				return TriState.TRUE;
			}
			return TriState.DEFAULT;
		}
	}
}
