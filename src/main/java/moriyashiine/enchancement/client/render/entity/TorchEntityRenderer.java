/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.render.entity;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.projectile.TorchEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class TorchEntityRenderer extends ProjectileEntityRenderer<TorchEntity> {
	private static final Identifier TEXTURE = Enchancement.id("textures/entity/projectiles/torch.png");

	public TorchEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(TorchEntity entity) {
		return TEXTURE;
	}
}
