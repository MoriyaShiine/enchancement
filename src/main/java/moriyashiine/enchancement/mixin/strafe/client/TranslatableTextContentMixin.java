/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.strafe.client;

import moriyashiine.enchancement.client.EnchancementClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(TranslatableTextContent.class)
public class TranslatableTextContentMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Object[] enchancement$strafe(Object[] value, String key) {
		if (value.length == 0 && key.equals("enchantment.enchancement.strafe.desc")) {
			value = new Object[1];
			value[0] = Text.translatable((EnchancementClient.STRAFE_KEYBINDING.isUnbound() ? MinecraftClient.getInstance().options.sprintKey : EnchancementClient.STRAFE_KEYBINDING).getTranslationKey()).formatted(Formatting.GOLD);
		}
		return value;
	}
}
