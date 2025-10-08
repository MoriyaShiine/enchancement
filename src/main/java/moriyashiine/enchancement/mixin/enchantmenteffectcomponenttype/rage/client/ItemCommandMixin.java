/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import moriyashiine.enchancement.client.render.item.RageRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(OrderedRenderCommandQueueImpl.ItemCommand.class)
public class ItemCommandMixin implements RageRenderState.Command {
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
