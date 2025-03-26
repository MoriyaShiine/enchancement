/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util.client;

import moriyashiine.enchancement.client.render.entity.state.EntityRenderStateAddition;
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
	private void enchancement$livingEntityRenderStateAddition(T entity, S state, float tickProgress, CallbackInfo ci) {
		EntityRenderStateAddition stateAddition = (EntityRenderStateAddition) state;
		stateAddition.enchancement$setActiveStack(entity instanceof LivingEntity living ? living.getActiveItem() : ItemStack.EMPTY);
		stateAddition.enchancement$setMainHandStack(entity instanceof LivingEntity living ? living.getMainHandStack() : ItemStack.EMPTY);
		stateAddition.enchancement$setRandom(entity.getRandom());
		stateAddition.enchancement$setCanCameraSee(MinecraftClient.getInstance().getCameraEntity() instanceof LivingEntity living && living.canSee(entity));
		stateAddition.enchancement$setGlowing(entity.isGlowing());
		stateAddition.enchancement$setHidesLabels(EnchancementUtil.hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.HIDE_LABEL_BEHIND_WALLS));
	}
}
