/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.renderer.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;

public class FloatingTridentRenderState {
	public static final RenderStateDataKey<FloatingTridentRenderState> KEY = RenderStateDataKey.create(() -> "floating trident");

	public boolean floating = false;
	public final ItemClusterRenderState item = new ItemClusterRenderState();
}
