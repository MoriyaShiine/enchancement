/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.render.entity;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.projectile.AmethystShardEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.Identifier;

public class AmethystShardEntityRenderer extends ProjectileEntityRenderer<AmethystShardEntity, ProjectileEntityRenderState> {
	private static final Identifier TEXTURE = Enchancement.id("textures/entity/projectiles/amethyst_shard.png");

	public AmethystShardEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public ProjectileEntityRenderState createRenderState() {
		return new ProjectileEntityRenderState();
	}

	@Override
	protected Identifier getTexture(ProjectileEntityRenderState state) {
		return TEXTURE;
	}
}
