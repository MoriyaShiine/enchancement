/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.render.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class ExtraRenderState {
	public static final RenderStateDataKey<ExtraRenderState> KEY = RenderStateDataKey.create(() -> "extra");

	public ItemStack activeStack = ItemStack.EMPTY;
	public ItemStack mainHandStack = ItemStack.EMPTY;
	public @Nullable Random random = null;
	public boolean canCameraSee = false;
	public boolean glowing = false;
	public boolean hidesLabels = false;
}
