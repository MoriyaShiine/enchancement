/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModEntityTypeTags {
	public static final TagKey<EntityType<?>> BRIMSTONE_HITTABLE = TagKey.of(RegistryKeys.ENTITY_TYPE, Enchancement.id("brimstone_hittable"));
	public static final TagKey<EntityType<?>> BYPASSES_DECREASING_DAMAGE = TagKey.of(RegistryKeys.ENTITY_TYPE, Enchancement.id("bypasses_decreasing_damage"));
	public static final TagKey<EntityType<?>> CANNOT_BURY = TagKey.of(RegistryKeys.ENTITY_TYPE, Enchancement.id("cannot_bury"));
	public static final TagKey<EntityType<?>> CANNOT_DISARM = TagKey.of(RegistryKeys.ENTITY_TYPE, Enchancement.id("cannot_disarm"));
	public static final TagKey<EntityType<?>> CANNOT_FREEZE = TagKey.of(RegistryKeys.ENTITY_TYPE, Enchancement.id("cannot_freeze"));
	public static final TagKey<EntityType<?>> NO_LOYALTY = TagKey.of(RegistryKeys.ENTITY_TYPE, Enchancement.id("has_no_loyalty"));
	public static final TagKey<EntityType<?>> VEIL_IMMUNE = TagKey.of(RegistryKeys.ENTITY_TYPE, Enchancement.id("veil_immune"));
}
