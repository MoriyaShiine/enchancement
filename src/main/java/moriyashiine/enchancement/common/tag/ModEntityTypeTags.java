package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class ModEntityTypeTags {
	public static final TagKey<EntityType<?>> BRIMSTONE_HITTABLE = TagKey.of(Registries.ENTITY_TYPE.getKey(), Enchancement.id("brimstone_hittable"));
	public static final TagKey<EntityType<?>> BYPASSES_DECREASING_DAMAGE = TagKey.of(Registries.ENTITY_TYPE.getKey(), Enchancement.id("bypasses_decreasing_damage"));
	public static final TagKey<EntityType<?>> CANNOT_FREEZE = TagKey.of(Registries.ENTITY_TYPE.getKey(), Enchancement.id("cannot_freeze"));
	public static final TagKey<EntityType<?>> CANNOT_BURY = TagKey.of(Registries.ENTITY_TYPE.getKey(), Enchancement.id("cannot_bury"));
	public static final TagKey<EntityType<?>> NO_LOYALTY = TagKey.of(Registries.ENTITY_TYPE.getKey(), Enchancement.id("has_no_loyalty"));
	public static final TagKey<EntityType<?>> VEIL_IMMUNE = TagKey.of(Registries.ENTITY_TYPE.getKey(), Enchancement.id("veil_immune"));
}
