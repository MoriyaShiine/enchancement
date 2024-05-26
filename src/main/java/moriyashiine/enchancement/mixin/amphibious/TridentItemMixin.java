/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.amphibious;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.entity.ExtendedWaterComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentItem.class)
public class TridentItemMixin {
	@ModifyExpressionValue(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
	private boolean enchancement$amphibious(boolean value, ItemStack stack, World world, LivingEntity user) {
		ExtendedWaterComponent extendedWaterComponent = ModEntityComponents.EXTENDED_WATER.get(user);
		if (extendedWaterComponent.hasAmphibious() && extendedWaterComponent.getTicksWet() > 0) {
			return true;
		}
		return value;
	}

	@ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
	private boolean enchancement$amphibious(boolean value, World world, PlayerEntity user) {
		ExtendedWaterComponent extendedWaterComponent = ModEntityComponents.EXTENDED_WATER.get(user);
		if (extendedWaterComponent.hasAmphibious() && extendedWaterComponent.getTicksWet() > 0) {
			return true;
		}
		return value;
	}
}
