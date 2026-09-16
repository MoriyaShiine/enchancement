package moriyashiine.enchancement.client.renderer.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class LightningDashRenderState {
	public static final RenderStateDataKey<LightningDashRenderState> KEY = RenderStateDataKey.create(() -> "lightning dash");

	public boolean usingLightningDash = false;
}
