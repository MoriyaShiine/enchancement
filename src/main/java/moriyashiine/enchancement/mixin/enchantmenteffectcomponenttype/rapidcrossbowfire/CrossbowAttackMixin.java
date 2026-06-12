/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rapidcrossbowfire;

import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowAttack.class)
public class CrossbowAttackMixin<E extends Mob> {
	@Inject(method = "stop(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;J)V", at = @At("TAIL"))
	private void enchancement$rapidCrossbowFire(ServerLevel level, E body, long timestamp, CallbackInfo ci) {
		ItemStack stack = body.getItemInHand(ProjectileUtil.getWeaponHoldingHand(body, Items.CROSSBOW));
		if (EnchantmentHelper.has(stack, EnchancementEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE)) {
			stack.releaseUsing(body.level(), body, body.getUseItemRemainingTicks());
		}
	}

	@Inject(method = "crossbowAttack", at = @At("HEAD"), cancellable = true)
	private void enchancement$rapidCrossbowFire(E body, LivingEntity target, CallbackInfo ci) {
		ItemStack stack = body.getItemInHand(ProjectileUtil.getWeaponHoldingHand(body, Items.CROSSBOW));
		if (EnchantmentHelper.has(stack, EnchancementEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE)) {
			body.startUsingItem(ProjectileUtil.getWeaponHoldingHand(body, Items.CROSSBOW));
			ci.cancel();
		}
	}
}
