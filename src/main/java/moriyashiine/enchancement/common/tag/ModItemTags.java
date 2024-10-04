/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModItemTags {
	public static final TagKey<Item> CANNOT_AUTOMATICALLY_CONSUME = TagKey.of(RegistryKeys.ITEM, Enchancement.id("cannot_automatically_consume"));
	public static final TagKey<Item> NO_LOYALTY = TagKey.of(RegistryKeys.ITEM, Enchancement.id("no_loyalty"));
	public static final TagKey<Item> RETAINS_DURABILITY = TagKey.of(RegistryKeys.ITEM, Enchancement.id("retains_durability"));
	public static final TagKey<Item> STRONGLY_ENCHANTED = TagKey.of(RegistryKeys.ITEM, Enchancement.id("strongly_enchanted"));
	public static final TagKey<Item> WEAKLY_ENCHANTED = TagKey.of(RegistryKeys.ITEM, Enchancement.id("weakly_enchanted"));
}
