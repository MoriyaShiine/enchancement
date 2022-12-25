/*
 * All Rights Reserved (c) 2022 MoriyaShiine
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
	@ModifyVariable(method = "<init>*", at = @At("HEAD"), argsOnly = true)
	private static String enchancement$redirectKey(String value) {
		if (shouldRedirect(value)) {
			return value + "_redirect";
		}
		return value;
	}

	@Unique
	private static boolean shouldRedirect(String key) {
		return switch (key) {
			case "enchantment.minecraft.fire_aspect.desc" -> ModConfig.fireAspectWorksAsFlintAndSteel;
			case "enchantment.minecraft.infinity.desc" -> ModConfig.allowInfinityOnCrossbows;
			case "enchantment.minecraft.channeling.desc" -> ModConfig.channelingWorksWhenNotThundering;
			case "enchantment.minecraft.luck_of_the_sea.desc" -> ModConfig.luckOfTheSeaHasLure;
			case "enchantment.minecraft.unbreaking.desc" -> ModConfig.unbreakingChangesFlag > 0;
			case "advancements.adventure.two_birds_one_arrow.description" ->
					ModConfig.allowedEnchantments.contains("enchancement:brimstone");
			default -> false;
		};
	}
}
