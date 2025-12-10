/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util.client;

import moriyashiine.enchancement.client.render.entity.state.ExtraRenderState;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
	@Inject(method = "updateRenderState", at = @At("TAIL"))
	private void enchancement$extraRenderState(T entity, S state, float tickProgress, CallbackInfo ci) {
		ExtraRenderState extraRenderState = new ExtraRenderState();
		extraRenderState.activeStack = entity instanceof LivingEntity living ? living.getActiveItem() : ItemStack.EMPTY;
		extraRenderState.random = entity.getRandom();
		extraRenderState.canCameraSee = MinecraftClient.getInstance().getCameraEntity() instanceof LivingEntity living && living.canSee(entity);
		extraRenderState.glowing = entity.isGlowing();
		extraRenderState.hidesLabels = EnchancementUtil.hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.HIDE_LABEL_BEHIND_WALLS);
		state.setData(ExtraRenderState.KEY, extraRenderState);
	}
}
