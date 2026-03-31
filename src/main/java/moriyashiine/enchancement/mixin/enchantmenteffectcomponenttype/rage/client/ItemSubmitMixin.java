/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import moriyashiine.enchancement.client.renderer.item.state.RageRenderState;
import net.minecraft.client.renderer.SubmitNodeStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SubmitNodeStorage.ItemSubmit.class)
public class ItemSubmitMixin implements RageRenderState.Submit {
	@Unique
	private int color;

	@Override
	public int enchancement$getColor() {
		return color;
	}

	@Override
	public void enchancement$setColor(int color) {
		this.color = color;
	}
}
