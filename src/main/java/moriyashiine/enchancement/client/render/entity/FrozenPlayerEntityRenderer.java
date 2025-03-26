/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.render.entity;

import moriyashiine.enchancement.client.render.entity.model.FrozenPlayerEntityModel;
import moriyashiine.enchancement.client.render.entity.state.FrozenPlayerEntityRenderState;
import moriyashiine.enchancement.common.entity.mob.FrozenPlayerEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FrozenPlayerEntityRenderer extends BipedEntityRenderer<FrozenPlayerEntity, FrozenPlayerEntityRenderState, FrozenPlayerEntityModel> {
	private static final Identifier DEFAULT_SKIN = Identifier.ofVanilla("textures/entity/player/wide/steve.png"), SLIM_SKIN = DefaultSkinHelper.getTexture();

	public FrozenPlayerEntityRenderer(EntityRendererFactory.Context ctx, boolean slim) {
		super(ctx, new FrozenPlayerEntityModel(slim ? ctx.getPart(FrozenPlayerEntityModel.LAYER_SLIM) : ctx.getPart(FrozenPlayerEntityModel.LAYER), slim), 0.5F);
	}

	@Override
	public FrozenPlayerEntityRenderState createRenderState() {
		return new FrozenPlayerEntityRenderState();
	}

	@Override
	public Identifier getTexture(FrozenPlayerEntityRenderState state) {
		return state.slim ? SLIM_SKIN : DEFAULT_SKIN;
	}

	@Override
	public void updateRenderState(FrozenPlayerEntity entity, FrozenPlayerEntityRenderState state, float tickProgress) {
		super.updateRenderState(entity, state, tickProgress);
		state.slim = entity.isSlim();
	}

	@Override
	protected void scale(FrozenPlayerEntityRenderState state, MatrixStack matrices) {
		matrices.scale(0.9375F, 0.9375F, 0.9375F);
	}
}
