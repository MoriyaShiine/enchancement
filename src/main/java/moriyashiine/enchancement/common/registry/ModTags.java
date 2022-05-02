package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class ModTags {
	public static class EntityTypes {
		public static final Tag<EntityType<?>> CANNOT_FREEZE = TagFactory.ENTITY_TYPE.create(new Identifier(Enchancement.MOD_ID, "cannot_freeze"));
		public static final Tag<EntityType<?>> CANNOT_BURY = TagFactory.ENTITY_TYPE.create(new Identifier(Enchancement.MOD_ID, "cannot_bury"));
	}

	public static class Blocks {
		public static final Tag<Block> ORES = TagFactory.BLOCK.create(new Identifier("c", "ores"));
		public static final Tag<Block> BURIABLE = TagFactory.BLOCK.create(new Identifier(Enchancement.MOD_ID, "buriable"));
		public static final Tag<Block> NETHER_ORES = TagFactory.BLOCK.create(new Identifier(Enchancement.MOD_ID, "nether_ores"));
		public static final Tag<Block> END_ORES = TagFactory.BLOCK.create(new Identifier(Enchancement.MOD_ID, "end_ores"));
	}

	public static class Items {
		public static final Tag<Item> RETAINS_DURABILITY = TagFactory.ITEM.create(new Identifier(Enchancement.MOD_ID, "retains_durability"));
	}
}
