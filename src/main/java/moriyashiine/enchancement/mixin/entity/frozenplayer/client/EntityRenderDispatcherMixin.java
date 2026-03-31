/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.entity.frozenplayer.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.renderer.entity.FrozenPlayerRenderer;
import moriyashiine.enchancement.client.renderer.entity.state.FrozenPlayerEntityRenderState;
import moriyashiine.enchancement.common.world.entity.decoration.FrozenPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unchecked")
@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	@Unique
	private static EntityRenderer<?, ?> DEFAULT_FROZEN_PLAYER_ENTITY_RENDERER, SLIM_FROZEN_PLAYER_ENTITY_RENDERER;

	@ModifyReturnValue(method = "getRenderer(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;", at = @At("RETURN"))
	private <T extends Entity> EntityRenderer<? super T, ?> enchancement$frozenPlayer(EntityRenderer<? super T, ?> original, T entity) {
		if (entity instanceof FrozenPlayer frozenPlayer) {
			return (EntityRenderer<? super T, ?>) getRenderer(frozenPlayer.isSlim());
		}
		return original;
	}

	@ModifyReturnValue(method = "getRenderer(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)Lnet/minecraft/client/renderer/entity/EntityRenderer;", at = @At("RETURN"))
	private <S extends EntityRenderState> EntityRenderer<?, ? super S> enchancement$frozenPlayer(EntityRenderer<?, ? super S> original, S entityRenderState) {
		if (entityRenderState instanceof FrozenPlayerEntityRenderState frozenPlayerState) {
			return (EntityRenderer<?, ? super S>) getRenderer(frozenPlayerState.slim);
		}
		return original;
	}

	@Inject(method = "onResourceManagerReload", at = @At("TAIL"))
	private void enchancement$frozenPlayer(ResourceManager resourceManager, CallbackInfo ci, @Local(name = "context") EntityRendererProvider.Context context) {
		DEFAULT_FROZEN_PLAYER_ENTITY_RENDERER = new FrozenPlayerRenderer(context, false);
		SLIM_FROZEN_PLAYER_ENTITY_RENDERER = new FrozenPlayerRenderer(context, true);
	}

	@Unique
	private EntityRenderer<?, ?> getRenderer(boolean slim) {
		return slim ? SLIM_FROZEN_PLAYER_ENTITY_RENDERER : DEFAULT_FROZEN_PLAYER_ENTITY_RENDERER;
	}
}
