/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;

public class ModMobEffectTags {
	public static final TagKey<MobEffect> CHAOS_UNCHOOSABLE = TagKey.create(Registries.MOB_EFFECT, Enchancement.id("chaos_unchoosable"));
}
