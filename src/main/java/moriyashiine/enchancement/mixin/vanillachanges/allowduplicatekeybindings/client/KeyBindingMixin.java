/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.allowduplicatekeybindings.client;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
	@Inject(method = "onKeyPressed", at = @At("TAIL"))
	private static void enchancement$allowDuplicateKeybindings(InputUtil.Key key, CallbackInfo ci, @Local KeyBinding keyBinding) {
		if (EnchancementClientUtil.allowDuplicateKeybinding(keyBinding)) {
			for (KeyBinding keyBinding2 : MinecraftClient.getInstance().options.allKeys) {
				if (keyBinding != keyBinding2 && keyBinding.equals(keyBinding2) && EnchancementClientUtil.allowDuplicateKeybinding(keyBinding2)) {
					keyBinding2.timesPressed++;
				}
			}
		}
	}

	@Inject(method = "setKeyPressed", at = @At("TAIL"))
	private static void enchancement$allowDuplicateKeybindings(InputUtil.Key key, boolean pressed, CallbackInfo ci, @Local KeyBinding keyBinding) {
		if (EnchancementClientUtil.allowDuplicateKeybinding(keyBinding)) {
			for (KeyBinding keyBinding2 : MinecraftClient.getInstance().options.allKeys) {
				if (keyBinding != keyBinding2 && keyBinding.equals(keyBinding2) && EnchancementClientUtil.allowDuplicateKeybinding(keyBinding2)) {
					keyBinding2.setPressed(pressed);
				}
			}
		}
	}
}
