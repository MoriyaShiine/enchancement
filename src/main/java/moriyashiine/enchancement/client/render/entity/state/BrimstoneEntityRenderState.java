/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.render.entity.state;

import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;

public class BrimstoneEntityRenderState extends ProjectileEntityRenderState {
	public int distanceTraveled = 0, ticksExisted = 0;
	public float damage = 0, damageMultiplier = 0;
}
