/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModDamageTypeTags {
	public static final TagKey<DamageType> BYPASSES_WARDENSPINE = TagKey.of(RegistryKeys.DAMAGE_TYPE, Enchancement.id("bypasses_wardenspine"));
}
