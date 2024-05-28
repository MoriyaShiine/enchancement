package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class ModEnchantmentTags {
	public static final TagKey<Enchantment> ALWAYS_SELECTABLE = TagKey.of(Registries.ENCHANTMENT.getKey(), Enchancement.id("always_selectable"));
	public static final TagKey<Enchantment> DISALLOWS_TOGGLEABLE_PASSIVE = TagKey.of(Registries.ENCHANTMENT.getKey(), Enchancement.id("disallows_toggleable_passive"));
	public static final TagKey<Enchantment> NEVER_SELECTABLE = TagKey.of(Registries.ENCHANTMENT.getKey(), Enchancement.id("never_selectable"));
}
