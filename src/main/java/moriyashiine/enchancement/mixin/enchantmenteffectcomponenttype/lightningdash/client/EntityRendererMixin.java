/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.lightningdash.client;

import moriyashiine.enchancement.client.renderer.entity.state.LightningDashRenderState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
	@Inject(method = "extractRenderState", at = @At("TAIL"))
	private void enchancement$lightningDash(T entity, S state, float partialTicks, CallbackInfo ci) {
		LightningDashRenderState lightningDashRenderState = new LightningDashRenderState();
		lightningDashRenderState.activeStack = entity instanceof LivingEntity living ? living.getUseItem() : ItemStack.EMPTY;
		lightningDashRenderState.random = entity.getRandom();
		state.setData(LightningDashRenderState.KEY, lightningDashRenderState);
	}
}
