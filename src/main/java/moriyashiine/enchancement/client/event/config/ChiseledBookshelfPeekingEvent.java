/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.config;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModBlockComponents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;

import java.util.List;

public class ChiseledBookshelfPeekingEvent implements HudLayerRegistrationCallback {
	@Override
	public void register(LayeredDrawerWrapper layeredDrawer) {
		layeredDrawer.attachLayerAfter(IdentifiedLayer.CROSSHAIR, Enchancement.id("chiseled_bookshelf_peeking"), (context, tickCounter) -> {
			if (ModConfig.chiseledBookshelfPeeking) {
				int width = context.getScaledWindowWidth() / 2;
				int height = context.getScaledWindowHeight() / 2;
				MinecraftClient client = MinecraftClient.getInstance();
				if (client.crosshairTarget instanceof BlockHitResult result && client.world.getBlockEntity(result.getBlockPos()) instanceof ChiseledBookshelfBlockEntity chiseledBookshelfBlockEntity) {
					((ChiseledBookshelfBlock) Blocks.CHISELED_BOOKSHELF).getSlotForHitPos(result, chiseledBookshelfBlockEntity.getCachedState()).ifPresent(index -> {
						List<ItemStack> stacks = ModBlockComponents.CHISELED_BOOKSHELF.get(chiseledBookshelfBlockEntity).getStacks();
						if (!stacks.isEmpty()) {
							ItemStack stack = stacks.get(index);
							if (!stack.isEmpty()) {
								context.drawItemTooltip(client.textRenderer, stack, width, height);
							}
						}
					});
				}
			}
		});
	}
}
