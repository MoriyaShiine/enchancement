package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModTags {
	public static class EntityTypes {
		public static final TagKey<EntityType<?>> CANNOT_FREEZE = TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(Enchancement.MOD_ID, "cannot_freeze"));
		public static final TagKey<EntityType<?>> CANNOT_BURY = TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(Enchancement.MOD_ID, "cannot_bury"));
	}

	public static class Blocks {
		public static final TagKey<Block> BURIABLE = TagKey.of(Registry.BLOCK_KEY, new Identifier(Enchancement.MOD_ID, "buriable"));
	}
}
