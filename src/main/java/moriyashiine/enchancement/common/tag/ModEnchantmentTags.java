/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModEnchantmentTags {
	public static final TagKey<Enchantment> ALWAYS_SELECTABLE = TagKey.of(RegistryKeys.ENCHANTMENT, Enchancement.id("always_selectable"));
	public static final TagKey<Enchantment> AUTOMATICALLY_FEEDS = TagKey.of(RegistryKeys.ENCHANTMENT, Enchancement.id("automatically_feeds"));
	public static final TagKey<Enchantment> DISALLOWS_TOGGLEABLE_PASSIVE = TagKey.of(RegistryKeys.ENCHANTMENT, Enchancement.id("disallows_toggleable_passive"));
	public static final TagKey<Enchantment> FREEZES_ENTITIES = TagKey.of(RegistryKeys.ENCHANTMENT, Enchancement.id("freezes_entities"));
	public static final TagKey<Enchantment> NEVER_SELECTABLE = TagKey.of(RegistryKeys.ENCHANTMENT, Enchancement.id("never_selectable"));

	public static final TagKey<Enchantment> BOUNCY_EXCLUSIVE_SET = TagKey.of(RegistryKeys.ENCHANTMENT, Enchancement.id("exclusive_set/bouncy"));
	public static final TagKey<Enchantment> BRIMSTONE_EXCLUSIVE_SET = TagKey.of(RegistryKeys.ENCHANTMENT, Enchancement.id("exclusive_set/brimstone"));
	public static final TagKey<Enchantment> FROSTBITE_EXCLUSIVE_SET = TagKey.of(RegistryKeys.ENCHANTMENT, Enchancement.id("exclusive_set/frostbite"));
	public static final TagKey<Enchantment> SILK_TOUCH_EXCLUSIVE_SET = TagKey.of(RegistryKeys.ENCHANTMENT, Enchancement.id("exclusive_set/silk_touch"));
	public static final TagKey<Enchantment> WARDENSPINE_EXCLUSIVE_SET = TagKey.of(RegistryKeys.ENCHANTMENT, Enchancement.id("exclusive_set/wardenspine"));
}
