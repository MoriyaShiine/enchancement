/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.renderer.item.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class RageRenderState {
	public static final RenderStateDataKey<RageRenderState> KEY = RenderStateDataKey.create(() -> "rage");

	public int color = -1;

	public interface Submit {
		int enchancement$getColor();

		void enchancement$setColor(int color);
	}
}
