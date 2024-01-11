/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
	public static class Blocks {
		public static final TagKey<Block> NETHER_ORES = TagKey.of(Registries.BLOCK.getKey(), Enchancement.id("nether_ores"));
		public static final TagKey<Block> END_ORES = TagKey.of(Registries.BLOCK.getKey(), Enchancement.id("end_ores"));
		public static final TagKey<Block> SMELTS_SELF = TagKey.of(Registries.BLOCK.getKey(), Enchancement.id("smelts_self"));
		public static final TagKey<Block> BURIABLE = TagKey.of(Registries.BLOCK.getKey(), Enchancement.id("buriable"));
	}

	public static class Enchantments {
		public static final TagKey<Enchantment> UNSELECTABLE = TagKey.of(Registries.ENCHANTMENT.getKey(), Enchancement.id("unselectable"));
	}

	public static class EntityTypes {
		public static final TagKey<EntityType<?>> BYPASSES_DECREASING_DAMAGE = TagKey.of(Registries.ENTITY_TYPE.getKey(), Enchancement.id("bypasses_decreasing_damage"));
		public static final TagKey<EntityType<?>> CANNOT_FREEZE = TagKey.of(Registries.ENTITY_TYPE.getKey(), Enchancement.id("cannot_freeze"));
		public static final TagKey<EntityType<?>> CANNOT_BURY = TagKey.of(Registries.ENTITY_TYPE.getKey(), Enchancement.id("cannot_bury"));
		public static final TagKey<EntityType<?>> NO_LOYALTY = TagKey.of(Registries.ENTITY_TYPE.getKey(), Enchancement.id("has_no_loyalty"));
		public static final TagKey<EntityType<?>> VEIL_IMMUNE = TagKey.of(Registries.ENTITY_TYPE.getKey(), Enchancement.id("veil_immune"));
	}

	public static class Items {
		public static final TagKey<Item> CANNOT_ASSIMILATE = TagKey.of(Registries.ITEM.getKey(), Enchancement.id("cannot_assimilate"));
		public static final TagKey<Item> NO_LOYALTY = TagKey.of(Registries.ITEM.getKey(), Enchancement.id("no_loyalty"));
		public static final TagKey<Item> RETAINS_DURABILITY = TagKey.of(Registries.ITEM.getKey(), Enchancement.id("retains_durability"));
	}

	public static class StatusEffects {
		public static final TagKey<StatusEffect> CHAOS_UNCHOOSABLE = TagKey.of(Registries.STATUS_EFFECT.getKey(), Enchancement.id("chaos_unchoosable"));
	}
}
