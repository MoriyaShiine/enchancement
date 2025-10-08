/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import moriyashiine.enchancement.client.render.item.RageRenderState;
import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HeldItemContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelManager.class)
public class ItemModelManagerMixin {
	@Inject(method = "update", at = @At("TAIL"))
	private void enchancement$rage(ItemRenderState renderState, ItemStack stack, ItemDisplayContext displayContext, World world, HeldItemContext heldItemContext, int seed, CallbackInfo ci) {
		RageRenderState rageRenderState = new RageRenderState();
		if (heldItemContext != null && heldItemContext.getEntity() instanceof LivingEntity living) {
			rageRenderState.color = RageEffect.getColor(living, stack);
		}
		renderState.setData(RageRenderState.KEY, rageRenderState);
	}
}
