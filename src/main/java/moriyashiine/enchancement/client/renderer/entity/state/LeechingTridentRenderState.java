/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.renderer.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class LeechingTridentRenderState {
	public static final RenderStateDataKey<LeechingTridentRenderState> KEY = RenderStateDataKey.create(() -> "leeching trident");

	public boolean active = false;
	public float offsetX = 0, offsetZ = 0, rotationY = 0, stabTicks = 0;
}
