/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.util.client;

import moriyashiine.enchancement.client.renderer.entity.state.ExtraRenderState;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.Minecraft;
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
	private void enchancement$extraRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
		ExtraRenderState extraRenderState = new ExtraRenderState();
		extraRenderState.activeStack = entity instanceof LivingEntity living ? living.getUseItem() : ItemStack.EMPTY;
		extraRenderState.random = entity.getRandom();
		extraRenderState.canCameraSee = Minecraft.getInstance().getCameraEntity() instanceof LivingEntity living && living.hasLineOfSight(entity);
		extraRenderState.glowing = entity.isCurrentlyGlowing();
		extraRenderState.hideName = EnchancementUtil.hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.HIDE_NAME_BEHIND_WALLS);
		state.setData(ExtraRenderState.KEY, extraRenderState);
	}
}
