/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EnchantableComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public class AnimalArmorEnchantmentEvent {
	public static class AllowComponent implements DefaultItemComponentEvents.ModifyCallback {
		@Override
		public void modify(DefaultItemComponentEvents.ModifyContext context) {
			if (ModConfig.rebalanceEquipment) {
				context.modify(item -> item instanceof AnimalArmorItem, (builder, item) -> builder.add(DataComponentTypes.ENCHANTABLE, new EnchantableComponent(1)));
			}
		}
	}

	public static class AllowEnchanting implements EnchantmentEvents.AllowEnchanting {
		@Override
		public TriState allowEnchanting(RegistryEntry<Enchantment> registryEntry, ItemStack itemStack, EnchantingContext enchantingContext) {
			if (ModConfig.rebalanceEquipment && itemStack.getItem() instanceof AnimalArmorItem && registryEntry.isIn(ModEnchantmentTags.ANIMAL_ARMOR_ENCHANTMENTS)) {
				return TriState.TRUE;
			}
			return TriState.DEFAULT;
		}
	}
}
