/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.client.render.entity;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.projectile.TorchEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TorchEntityRenderer extends ProjectileEntityRenderer<TorchEntity> {
	private static final Identifier TEXTURE = new Identifier(Enchancement.MOD_ID, "textures/entity/projectiles/torch.png");

	public TorchEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(TorchEntity entity) {
		return TEXTURE;
	}
}
