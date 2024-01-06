/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.slide.client;

import moriyashiine.enchancement.client.EnchancementClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableTextContent.class)
public class TranslatableTextContentMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Object[] enchancement$slide(Object[] value, String key) {
		if (value.length == 0 && key.equals("enchantment.enchancement.slide.desc")) {
			value = new Object[2];
			value[0] = Text.translatable((EnchancementClient.SLIDE_KEYBINDING.isUnbound() ? MinecraftClient.getInstance().options.sprintKey : EnchancementClient.SLIDE_KEYBINDING).getTranslationKey()).formatted(Formatting.GOLD);
			value[1] = Text.translatable((EnchancementClient.SLAM_KEYBINDING.isUnbound() ? MinecraftClient.getInstance().options.sprintKey : EnchancementClient.SLAM_KEYBINDING).getTranslationKey()).formatted(Formatting.GOLD);
		}
		return value;
	}
}
