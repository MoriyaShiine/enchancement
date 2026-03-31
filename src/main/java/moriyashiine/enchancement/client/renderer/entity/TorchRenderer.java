/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.renderer.entity;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.Torch;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;

public class TorchRenderer extends ArrowRenderer<Torch, ArrowRenderState> {
	private static final Identifier TEXTURE = Enchancement.id("textures/entity/projectiles/torch.png");

	public TorchRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ArrowRenderState createRenderState() {
		return new ArrowRenderState();
	}

	@Override
	protected Identifier getTextureLocation(ArrowRenderState state) {
		return TEXTURE;
	}
}
