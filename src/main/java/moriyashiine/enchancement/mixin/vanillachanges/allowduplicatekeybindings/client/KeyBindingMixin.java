/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.allowduplicatekeybindings.client;

import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
	@Inject(method = "onKeyPressed", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void enchancement$allowDuplicateKeybindings(InputUtil.Key key, CallbackInfo ci, KeyBinding keyBinding) {
		if (EnchancementClientUtil.allowDuplicateKeybinding(keyBinding)) {
			for (KeyBinding keyBinding2 : MinecraftClient.getInstance().options.allKeys) {
				if (keyBinding != keyBinding2 && keyBinding.equals(keyBinding2) && EnchancementClientUtil.allowDuplicateKeybinding(keyBinding2)) {
					keyBinding2.timesPressed++;
				}
			}
		}
	}

	@Inject(method = "setKeyPressed", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void enchancement$allowDuplicateKeybindings(InputUtil.Key key, boolean pressed, CallbackInfo ci, KeyBinding keyBinding) {
		if (EnchancementClientUtil.allowDuplicateKeybinding(keyBinding)) {
			for (KeyBinding keyBinding2 : MinecraftClient.getInstance().options.allKeys) {
				if (keyBinding != keyBinding2 && keyBinding.equals(keyBinding2) && EnchancementClientUtil.allowDuplicateKeybinding(keyBinding2)) {
					keyBinding2.setPressed(pressed);
				}
			}
		}
	}
}
