/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.entity.frozenplayer.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.render.entity.FrozenPlayerEntityRenderer;
import moriyashiine.enchancement.common.entity.mob.FrozenPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	@Unique
	private static EntityRenderer<?, ?> DEFAULT_FROZEN_PLAYER_ENTITY_RENDERER, SLIM_FROZEN_PLAYER_ENTITY_RENDERER;

	@SuppressWarnings("unchecked")
	@ModifyReturnValue(method = "getRenderer", at = @At("RETURN"))
	private <T extends Entity> EntityRenderer<? super T, ?> enchancement$frozenPlayer(EntityRenderer<? super T, ?> original, T entity) {
		if (entity instanceof FrozenPlayerEntity frozenPlayer) {
			return (EntityRenderer<? super T, ?>) (frozenPlayer.isSlim() ? SLIM_FROZEN_PLAYER_ENTITY_RENDERER : DEFAULT_FROZEN_PLAYER_ENTITY_RENDERER);
		}
		return original;
	}

	@Inject(method = "reload", at = @At("TAIL"))
	private void enchancement$frozenPlayer(ResourceManager manager, CallbackInfo ci, @Local EntityRendererFactory.Context context) {
		DEFAULT_FROZEN_PLAYER_ENTITY_RENDERER = new FrozenPlayerEntityRenderer(context, false);
		SLIM_FROZEN_PLAYER_ENTITY_RENDERER = new FrozenPlayerEntityRenderer(context, true);
	}
}
