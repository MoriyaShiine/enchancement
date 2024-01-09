/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.allowduplicatekeybindings.client;

import moriyashiine.enchancement.common.ModConfig;
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

	@ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;setMessage(Lnet/minecraft/text/Text;)V", ordinal = 1))
	private Text enchancement$allowDuplicateKeybindings(Text value) {
		if (ModConfig.allowDuplicateKeybindings) {
			return binding.getBoundKeyLocalizedText().copy().formatted(Formatting.AQUA);
		}
		return value;
	}
}
