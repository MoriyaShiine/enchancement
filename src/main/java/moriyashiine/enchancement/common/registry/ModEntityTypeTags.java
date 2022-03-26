package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntityTypeTags {
	public static final TagKey<EntityType<?>> UNFREEZABLE = TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(Enchancement.MOD_ID, "unfreezable"));
}
