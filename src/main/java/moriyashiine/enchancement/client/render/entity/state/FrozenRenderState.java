/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.render.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class FrozenRenderState {
	public static final RenderStateDataKey<FrozenRenderState> KEY = RenderStateDataKey.create(() -> "frozen");

	public boolean frozen = false;
}
