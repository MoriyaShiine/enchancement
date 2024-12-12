/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import moriyashiine.enchancement.client.render.entity.state.ItemStackEntityRenderStateAddition;
import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.state.ItemStackEntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackEntityRenderState.class)
public class ItemStackEntityRenderStateMixin implements ItemStackEntityRenderStateAddition {
	@Unique
	private int rageColor = -1;

	@Override
	public int enchancement$getRageColor() {
		return rageColor;
	}

	@Inject(method = "update", at = @At("TAIL"))
	private void enchancement$rage(Entity entity, ItemStack stack, ItemModelManager itemModelManager, CallbackInfo ci) {
		rageColor = RageEffect.getColor(MinecraftClient.getInstance().getCameraEntity(), stack);
	}
}
