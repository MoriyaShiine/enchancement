/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.renderer.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class ExtraRenderState {
	public static final RenderStateDataKey<ExtraRenderState> KEY = RenderStateDataKey.create(() -> "extra");

	public ItemStack activeStack = ItemStack.EMPTY;
	public @Nullable RandomSource random = null;
	public boolean canCameraSee = false;
	public boolean glowing = false;
	public boolean hideName = false;
}
