/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.brimstone.client;

import moriyashiine.enchancement.client.gui.hud.BrimstoneHudElement;
import moriyashiine.enchancement.common.world.item.effects.BrimstoneEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
	@Shadow
	public abstract int getUseDuration(ItemStack itemStack, LivingEntity user);

	@Inject(method = "onUseTick", at = @At("HEAD"))
	private void enchancement$brimstone(Level level, LivingEntity entity, ItemStack itemStack, int ticksRemaining, CallbackInfo ci) {
		if (SLibClientUtils.isHost(entity)) {
			BrimstoneHudElement.health = BrimstoneEffect.getBrimstoneDamage(CrossbowItem.getPowerForTime(getUseDuration(itemStack, entity) - ticksRemaining, itemStack, entity));
		}
	}
}
