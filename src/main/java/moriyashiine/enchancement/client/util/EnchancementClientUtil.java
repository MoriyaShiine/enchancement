/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.util;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;

import java.util.HashSet;
import java.util.Set;

public class EnchancementClientUtil {
	public static final Set<KeyBinding> VANILLA_AND_ENCHANCEMENT_KEYBINDINGS = new HashSet<>();

	public static boolean allowDuplicateKeybinding(KeyBinding keyBinding) {
		if (keyBinding == null) {
			return false;
		}
		return switch (ModConfig.allowDuplicateKeybindings) {
			case NONE -> false;
			case VANILLA_AND_ENCHANCEMENT -> VANILLA_AND_ENCHANCEMENT_KEYBINDINGS.contains(keyBinding);
			case ALL -> true;
		};
	}

	public static boolean shouldAddParticles(Entity entity) {
		return MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().getCameraEntity();
	}
}
