/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeTags {
	public static final TagKey<EntityType<?>> BRIMSTONE_HITTABLE = TagKey.create(Registries.ENTITY_TYPE, Enchancement.id("brimstone_hittable"));
	public static final TagKey<EntityType<?>> BYPASSES_DECREASING_DAMAGE = TagKey.create(Registries.ENTITY_TYPE, Enchancement.id("bypasses_decreasing_damage"));
	public static final TagKey<EntityType<?>> CANNOT_BURY = TagKey.create(Registries.ENTITY_TYPE, Enchancement.id("cannot_bury"));
	public static final TagKey<EntityType<?>> CANNOT_DISARM = TagKey.create(Registries.ENTITY_TYPE, Enchancement.id("cannot_disarm"));
	public static final TagKey<EntityType<?>> CANNOT_FREEZE = TagKey.create(Registries.ENTITY_TYPE, Enchancement.id("cannot_freeze"));
	public static final TagKey<EntityType<?>> NO_LOYALTY = TagKey.create(Registries.ENTITY_TYPE, Enchancement.id("has_no_loyalty"));
	public static final TagKey<EntityType<?>> VEIL_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, Enchancement.id("veil_immune"));
}
