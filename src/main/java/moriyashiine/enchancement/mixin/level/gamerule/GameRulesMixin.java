/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.level.gamerule;

import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRules.class)
public class GameRulesMixin {
	@ModifyVariable(method = "registerBoolean", at = @At("HEAD"), argsOnly = true)
	private static boolean enchancement$gameRule(boolean defaultValue, String id) {
		if (id.equals("player_movement_check")) {
			return false;
		}
		return defaultValue;
	}
}
