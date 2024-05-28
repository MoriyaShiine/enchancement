package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class ModStatusEffectTags {
	public static final TagKey<StatusEffect> CHAOS_UNCHOOSABLE = TagKey.of(Registries.STATUS_EFFECT.getKey(), Enchancement.id("chaos_unchoosable"));
}
