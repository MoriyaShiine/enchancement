/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModStatusEffectTags {
	public static final TagKey<StatusEffect> CHAOS_UNCHOOSABLE = TagKey.of(RegistryKeys.STATUS_EFFECT, Enchancement.id("chaos_unchoosable"));
}
