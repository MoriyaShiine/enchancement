/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantmentTags {
	public static final TagKey<Enchantment> ANIMAL_ARMOR_ENCHANTMENTS = TagKey.create(Registries.ENCHANTMENT, Enchancement.id("animal_armor_enchantments"));
	public static final TagKey<Enchantment> AUTOMATICALLY_FEEDS = TagKey.create(Registries.ENCHANTMENT, Enchancement.id("automatically_feeds"));
	public static final TagKey<Enchantment> DISALLOWS_TOGGLEABLE_PASSIVE = TagKey.create(Registries.ENCHANTMENT, Enchancement.id("disallows_toggleable_passive"));
	public static final TagKey<Enchantment> FREEZES_ENTITIES = TagKey.create(Registries.ENCHANTMENT, Enchancement.id("freezes_entities"));

	public static final TagKey<Enchantment> BOUNCY_EXCLUSIVE_SET = TagKey.create(Registries.ENCHANTMENT, Enchancement.id("exclusive_set/bouncy"));
	public static final TagKey<Enchantment> BRIMSTONE_EXCLUSIVE_SET = TagKey.create(Registries.ENCHANTMENT, Enchancement.id("exclusive_set/brimstone"));
	public static final TagKey<Enchantment> FROSTBITE_EXCLUSIVE_SET = TagKey.create(Registries.ENCHANTMENT, Enchancement.id("exclusive_set/frostbite"));
	public static final TagKey<Enchantment> MACE_EXCLUSIVE_SET = TagKey.create(Registries.ENCHANTMENT, Enchancement.id("exclusive_set/mace"));
	public static final TagKey<Enchantment> SILK_TOUCH_EXCLUSIVE_SET = TagKey.create(Registries.ENCHANTMENT, Enchancement.id("exclusive_set/silk_touch"));
	public static final TagKey<Enchantment> UNIQUE_CROSSBOW_PROJECTILE_EXCLUSIVE_SET = TagKey.create(Registries.ENCHANTMENT, Enchancement.id("exclusive_set/unique_crossbow_projectile"));
	public static final TagKey<Enchantment> WARDENSPINE_EXCLUSIVE_SET = TagKey.create(Registries.ENCHANTMENT, Enchancement.id("exclusive_set/wardenspine"));
}
