/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModTags {
	public static class Blocks {
		public static final TagKey<Block> NETHER_ORES = TagKey.of(Registry.BLOCK_KEY, new Identifier(Enchancement.MOD_ID, "nether_ores"));
		public static final TagKey<Block> END_ORES = TagKey.of(Registry.BLOCK_KEY, new Identifier(Enchancement.MOD_ID, "end_ores"));
		public static final TagKey<Block> SMELTS_SELF = TagKey.of(Registry.BLOCK_KEY, new Identifier(Enchancement.MOD_ID, "smelts_self"));
		public static final TagKey<Block> BURIABLE = TagKey.of(Registry.BLOCK_KEY, new Identifier(Enchancement.MOD_ID, "buriable"));
	}

	public static class Enchantments {
		public static final TagKey<Enchantment> UNSELECTABLE = TagKey.of(Registry.ENCHANTMENT_KEY, new Identifier(Enchancement.MOD_ID, "unselectable"));
	}

	public static class EntityTypes {
		public static final TagKey<EntityType<?>> CANNOT_FREEZE = TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(Enchancement.MOD_ID, "cannot_freeze"));
		public static final TagKey<EntityType<?>> CANNOT_BURY = TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(Enchancement.MOD_ID, "cannot_bury"));
	}

	public static class Items {
		public static final TagKey<Item> NO_LOYALTY = TagKey.of(Registry.ITEM_KEY, new Identifier(Enchancement.MOD_ID, "no_loyalty"));
		public static final TagKey<Item> RETAINS_DURABILITY = TagKey.of(Registry.ITEM_KEY, new Identifier(Enchancement.MOD_ID, "retains_durability"));
	}

	public static class StatusEffects {
		public static final TagKey<StatusEffect> CHAOS_UNCHOOSABLE = TagKey.of(Registry.MOB_EFFECT_KEY, new Identifier(Enchancement.MOD_ID, "chaos_unchoosable"));
	}
}
