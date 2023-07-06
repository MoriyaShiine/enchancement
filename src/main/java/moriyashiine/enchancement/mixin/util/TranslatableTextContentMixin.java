/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.util;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableTextContent.class)
public class TranslatableTextContentMixin {
	@ModifyVariable(method = "<init>*", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static String enchancement$redirectKey(String value) {
		return getRedirect(value);
	}

	@Unique
	private static String getRedirect(String key) {
		if (key.equals("enchantment.minecraft.channeling.desc")) {
			if (ModConfig.channelingIgnitesOnMelee && ModConfig.channelingWorksWhenNotThundering) {
				return key + ".redirect_melee_thunderless";
			} else if (ModConfig.channelingIgnitesOnMelee) {
				return key + ".redirect_melee";
			} else if (ModConfig.channelingWorksWhenNotThundering) {
				return key + ".redirect_thunderless";
			}
		}
		return switch (key) {
			case "enchantment.minecraft.fire_aspect.desc" ->
					ModConfig.fireAspectWorksAsFlintAndSteel ? key + ".redirect" : key;
			case "enchantment.minecraft.luck_of_the_sea.desc" ->
					ModConfig.luckOfTheSeaHasLure ? key + ".redirect" : key;
			case "enchantment.minecraft.unbreaking.desc" ->
					ModConfig.unbreakingChangesFlag > 0 ? key + ".redirect" : key;
			case "advancements.adventure.two_birds_one_arrow.description" ->
					ModConfig.allowedEnchantments.contains("enchancement:brimstone") ? key + ".redirect" : key;
			default -> key;
		};
	}
}
