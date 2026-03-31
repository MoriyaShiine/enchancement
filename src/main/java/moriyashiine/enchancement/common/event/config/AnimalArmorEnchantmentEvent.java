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
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Enchantment;

public class AnimalArmorEnchantmentEvent {
	public static class AllowComponent implements DefaultItemComponentEvents.ModifyCallback {
		@Override
		public void modify(DefaultItemComponentEvents.ModifyContext context) {
			if (ModConfig.rebalanceEquipment) {
				context.modify(item -> EnchancementUtil.isBodyArmor(item.getDefaultInstance()), (builder, _) -> builder.set(DataComponents.ENCHANTABLE, new Enchantable(1)));
			}
		}
	}

	public static class AllowEnchanting implements EnchantmentEvents.AllowEnchanting {
		@Override
		public TriState allowEnchanting(Holder<Enchantment> enchantment, ItemStack target, EnchantingContext enchantingContext) {
			if (ModConfig.rebalanceEquipment && EnchancementUtil.isBodyArmor(target) && enchantment.is(ModEnchantmentTags.ANIMAL_ARMOR_ENCHANTMENTS)) {
				return TriState.TRUE;
			}
			return TriState.DEFAULT;
		}
	}
}
