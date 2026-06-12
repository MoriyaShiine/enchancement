/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments.client;

import moriyashiine.enchancement.client.renderer.entity.state.EnchantedFireRenderState;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
	@Inject(method = "extractRenderState", at = @At("TAIL"))
	private void enchancement$rebalanceEnchantments(T entity, S state, float partialTicks, CallbackInfo ci) {
		EnchantedFireRenderState enchantedFireRenderState = new EnchantedFireRenderState();
		EnchancementEntityComponents.IGNITED.maybeGet(entity).ifPresent(ignited -> enchantedFireRenderState.renderEnchantedFire = ignited.isIgnited());
		state.setData(EnchantedFireRenderState.KEY, enchantedFireRenderState);
	}
}
