package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class ModItemTags {
	public static final TagKey<Item> CANNOT_ASSIMILATE = TagKey.of(Registries.ITEM.getKey(), Enchancement.id("cannot_assimilate"));
	public static final TagKey<Item> NO_LOYALTY = TagKey.of(Registries.ITEM.getKey(), Enchancement.id("no_loyalty"));
	public static final TagKey<Item> RETAINS_DURABILITY = TagKey.of(Registries.ITEM.getKey(), Enchancement.id("retains_durability"));
	public static final TagKey<Item> STRONGLY_ENCHANTED = TagKey.of(Registries.ITEM.getKey(), Enchancement.id("strongly_enchanted"));
	public static final TagKey<Item> WEAKLY_ENCHANTED = TagKey.of(Registries.ITEM.getKey(), Enchancement.id("weakly_enchanted"));
}
