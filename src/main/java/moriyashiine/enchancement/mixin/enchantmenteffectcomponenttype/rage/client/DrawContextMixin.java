/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.render.item.ItemRenderStateAddition;
import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {
	@Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/item/ItemModelManager;clearAndUpdate(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V"))
	private void enchancement$rage(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, CallbackInfo ci, @Local ItemRenderState itemRenderState) {
		int color = RageEffect.getColor(entity, stack);
		if (color != -1) {
			((ItemRenderStateAddition) itemRenderState).enchancement$setRageColor(color);
		}
	}
}
