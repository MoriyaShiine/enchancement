/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.misc;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableText.class)
public class TranslatableTextMixin {
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
			case "enchantment.minecraft.fire_aspect.desc" -> Enchancement.config.fireAspectWorksAsFlintAndSteel;
			case "enchantment.minecraft.infinity.desc" -> Enchancement.config.allowInfinityOnCrossbows;
			case "enchantment.minecraft.channeling.desc" -> Enchancement.config.channelingWorksWhenNotThundering;
			case "enchantment.minecraft.luck_of_the_sea.desc" -> Enchancement.config.luckOfTheSeaHasLure;
			case "enchantment.minecraft.unbreaking.desc" -> Enchancement.config.unbreakingChangesFlag > 0;
			default -> false;
		};
	}
}
