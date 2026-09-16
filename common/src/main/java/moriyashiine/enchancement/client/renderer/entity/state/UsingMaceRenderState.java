package moriyashiine.enchancement.client.renderer.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class UsingMaceRenderState {
	public static final RenderStateDataKey<UsingMaceRenderState> KEY = RenderStateDataKey.create(() -> "using mace");

	public boolean usingMace = false;
}
