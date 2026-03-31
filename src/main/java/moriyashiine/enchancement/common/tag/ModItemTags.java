/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
	public static final TagKey<Item> CANNOT_AUTOMATICALLY_CONSUME = TagKey.create(Registries.ITEM, Enchancement.id("cannot_automatically_consume"));
	public static final TagKey<Item> DEFAULT_ENCHANTING_MATERIAL = TagKey.create(Registries.ITEM, Enchancement.id("default_enchanting_material"));
	public static final TagKey<Item> NO_LOYALTY = TagKey.create(Registries.ITEM, Enchancement.id("no_loyalty"));
	public static final TagKey<Item> RETAINS_DURABILITY = TagKey.create(Registries.ITEM, Enchancement.id("retains_durability"));
	public static final TagKey<Item> STRONGLY_ENCHANTED = TagKey.create(Registries.ITEM, Enchancement.id("strongly_enchanted"));
	public static final TagKey<Item> WEAKLY_ENCHANTED = TagKey.create(Registries.ITEM, Enchancement.id("weakly_enchanted"));
}
