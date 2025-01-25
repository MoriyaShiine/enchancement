/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.extendedwaterspinattack;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public class TridentItemMixin {
	@ModifyExpressionValue(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
	private boolean enchancement$extendedWaterSpinAttack(boolean value, ItemStack stack, World world, LivingEntity user) {
		return value || shouldApply(user);
	}

	@Inject(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;useRiptide(IFLnet/minecraft/item/ItemStack;)V"))
	private void enchancement$extendedWaterSpinAttack(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
		if (shouldApply(user)) {
			ModEntityComponents.EXTENDED_WATER_TIME.get(user).decrement(60);
		}
	}

	@ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
	private boolean enchancement$extendedWaterSpinAttack(boolean value, World world, PlayerEntity user) {
		return value || shouldApply(user);
	}

	@Unique
	private static boolean shouldApply(LivingEntity user) {
		return EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.EXTENDED_WATER_SPIN_ATTACK) && ModEntityComponents.EXTENDED_WATER_TIME.get(user).getTicksWet() > 0;
	}
}
