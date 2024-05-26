/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.allowduplicatekeybindings.client;

import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value = GameOptions.class, priority = -1)
public class GameOptionsMixin {
	@Shadow
	@Final
	public KeyBinding[] allKeys;

	@Inject(method = "load", at = @At("HEAD"))
	private void enchancement$allowDuplicateKeybindings(CallbackInfo ci) {
		EnchancementClientUtil.VANILLA_AND_ENCHANCEMENT_KEYBINDINGS.addAll(Arrays.asList(allKeys));
	}
}
