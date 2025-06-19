/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.brimstone.client;

import moriyashiine.enchancement.client.hud.BrimstoneHudElement;
import moriyashiine.enchancement.common.enchantment.effect.BrimstoneEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
	@Shadow
	public abstract int getMaxUseTime(ItemStack stack, LivingEntity user);

	@Inject(method = "usageTick", at = @At("HEAD"))
	private void enchancement$brimstone(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
		if (SLibClientUtils.isHost(user)) {
			BrimstoneHudElement.health = BrimstoneEffect.getBrimstoneDamage(CrossbowItem.getPullProgress(getMaxUseTime(stack, user) - remainingUseTicks, stack, user));
		}
	}
}
