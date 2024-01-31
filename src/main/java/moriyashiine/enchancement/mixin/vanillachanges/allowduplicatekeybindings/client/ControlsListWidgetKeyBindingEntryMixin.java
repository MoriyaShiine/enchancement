/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.allowduplicatekeybindings.client;

import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ControlsListWidget.KeyBindingEntry.class)
public class ControlsListWidgetKeyBindingEntryMixin {
	@Shadow
	@Final
	private KeyBinding binding;

	@ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"), index = 4)
	private int enchancement$allowDuplicateKeybindings(int value) {
		if (EnchancementClientUtil.allowDuplicateKeybinding(binding)) {
			return Formatting.AQUA.getColorValue() | 0xFF000000;
		}
		return value;
	}

	@ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;setMessage(Lnet/minecraft/text/Text;)V", ordinal = 1))
	private Text enchancement$allowDuplicateKeybindings(Text value) {
		if (EnchancementClientUtil.allowDuplicateKeybinding(binding)) {
			return binding.getBoundKeyLocalizedText().copy().formatted(Formatting.AQUA);
		}
		return value;
	}
}
