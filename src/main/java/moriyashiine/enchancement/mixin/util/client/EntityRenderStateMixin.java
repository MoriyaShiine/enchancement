/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util.client;

import moriyashiine.enchancement.client.render.entity.state.EntityRenderStateAddition;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateAddition {
	@Unique
	private Entity entity;

	@Override
	public Entity enchancement$getEntity() {
		return entity;
	}

	@Override
	public void enchancement$setEntity(Entity entity) {
		this.entity = entity;
	}
}
