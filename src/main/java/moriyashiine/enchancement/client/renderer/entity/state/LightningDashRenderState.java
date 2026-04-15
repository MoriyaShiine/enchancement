/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.renderer.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class LightningDashRenderState {
	public static final RenderStateDataKey<LightningDashRenderState> KEY = RenderStateDataKey.create(() -> "lightning dash");

	public ItemStack activeStack = ItemStack.EMPTY;
	public @Nullable RandomSource random = null;
}
