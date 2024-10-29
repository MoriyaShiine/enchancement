/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.render.entity.state;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;

public class FloatingTridentRenderState {
	public boolean isFloating = false;
	public ItemStack stack = ItemStack.EMPTY;
	public BakedModel model = null;
}
