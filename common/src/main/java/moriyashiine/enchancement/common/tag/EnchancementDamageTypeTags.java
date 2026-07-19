package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class EnchancementDamageTypeTags {
	public static final TagKey<DamageType> BYPASSES_WARDENSPINE = TagKey.create(Registries.DAMAGE_TYPE, Enchancement.id("bypasses_wardenspine"));
	public static final TagKey<DamageType> DOES_NOT_INTERRUPT = TagKey.create(Registries.DAMAGE_TYPE, Enchancement.id("does_not_interrupt"));
	public static final TagKey<DamageType> IS_SAFE_FALL = TagKey.create(Registries.DAMAGE_TYPE, Enchancement.id("is_safe_fall"));
}
