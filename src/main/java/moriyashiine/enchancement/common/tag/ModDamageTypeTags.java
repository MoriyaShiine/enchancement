/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypeTags {
	public static final TagKey<DamageType> BYPASSES_WARDENSPINE = TagKey.create(Registries.DAMAGE_TYPE, Enchancement.id("bypasses_wardenspine"));
	public static final TagKey<DamageType> DOES_NOT_INTERRUPT = TagKey.create(Registries.DAMAGE_TYPE, Enchancement.id("does_not_interrupt"));
}
