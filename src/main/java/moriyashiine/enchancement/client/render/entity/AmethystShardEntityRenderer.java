/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.render.entity;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.projectile.AmethystShardEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AmethystShardEntityRenderer extends ProjectileEntityRenderer<AmethystShardEntity> {
	private static final Identifier TEXTURE = Enchancement.id("textures/entity/projectiles/amethyst_shard.png");

	public AmethystShardEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(AmethystShardEntity entity) {
		return TEXTURE;
	}
}
