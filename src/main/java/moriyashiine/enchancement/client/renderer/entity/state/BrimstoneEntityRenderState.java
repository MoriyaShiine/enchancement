/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.ArrowRenderState;

public class BrimstoneEntityRenderState extends ArrowRenderState {
	public int distanceTraveled = 0, ticksExisted = 0;
	public float damage = 0, damageMultiplier = 0;
}
