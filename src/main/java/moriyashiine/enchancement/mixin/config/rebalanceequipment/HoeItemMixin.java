/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.item.HoeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HoeItem.class)
public class HoeItemMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private static float enchancement$rebalanceEquipmentDamage(float attackDamageBaseline) {
		return ModConfig.rebalanceEquipment ? Math.max(0, attackDamageBaseline) + 1 : attackDamageBaseline;
	}

	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 1)
	private static float enchancement$rebalanceEquipmentSpeed(float attackSpeedBaseline) {
		return ModConfig.rebalanceEquipment ? -2 : attackSpeedBaseline;
	}
}
