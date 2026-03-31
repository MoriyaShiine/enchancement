/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.enchancement.client.renderer.entity.model.FrozenPlayerModel;
import moriyashiine.enchancement.client.renderer.entity.state.FrozenPlayerEntityRenderState;
import moriyashiine.enchancement.common.world.entity.decoration.FrozenPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.Identifier;

public class FrozenPlayerRenderer extends HumanoidMobRenderer<FrozenPlayer, FrozenPlayerEntityRenderState, FrozenPlayerModel> {
	private static final Identifier DEFAULT_SKIN = Identifier.withDefaultNamespace("textures/entity/player/wide/steve.png"), SLIM_SKIN = DefaultPlayerSkin.getDefaultTexture();

	public FrozenPlayerRenderer(EntityRendererProvider.Context context, boolean slim) {
		super(context, new FrozenPlayerModel(slim ? context.bakeLayer(FrozenPlayerModel.LAYER_SLIM) : context.bakeLayer(FrozenPlayerModel.LAYER), slim), 0.5F);
	}

	@Override
	public FrozenPlayerEntityRenderState createRenderState() {
		return new FrozenPlayerEntityRenderState();
	}

	@Override
	public Identifier getTextureLocation(FrozenPlayerEntityRenderState state) {
		return state.slim ? SLIM_SKIN : DEFAULT_SKIN;
	}

	@Override
	public void extractRenderState(FrozenPlayer entity, FrozenPlayerEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.slim = entity.isSlim();
	}

	@Override
	protected void scale(FrozenPlayerEntityRenderState state, PoseStack poseStack) {
		poseStack.scale(0.9375F, 0.9375F, 0.9375F);
	}
}
