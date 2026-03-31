/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.extendedwaterspinattack;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public class TridentItemMixin {
	@ModifyExpressionValue(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"))
	private boolean enchancement$extendedWaterSpinAttack(boolean value, ItemStack itemStack, Level level, LivingEntity entity) {
		return value || shouldApply(entity);
	}

	@Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startAutoSpinAttack(IFLnet/minecraft/world/item/ItemStack;)V"))
	private void enchancement$extendedWaterSpinAttack(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime, CallbackInfoReturnable<Boolean> cir) {
		if (shouldApply(entity)) {
			ModEntityComponents.EXTENDED_WATER_TIME.get(entity).decrement(60);
		}
	}

	@ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"))
	private boolean enchancement$extendedWaterSpinAttack(boolean value, Level level, Player player) {
		return value || shouldApply(player);
	}

	@Unique
	private static boolean shouldApply(LivingEntity user) {
		return EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.EXTENDED_WATER_SPIN_ATTACK) && ModEntityComponents.EXTENDED_WATER_TIME.get(user).getTicksWet() > 0;
	}
}
