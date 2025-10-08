/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.hud;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModBlockComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudStatusBarHeightRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;

import java.util.List;

public class ChiseledBookshelfPeekingHudElement implements HudElement {
	public static int windowHeightOffset = 0;

	@Override
	public void render(DrawContext context, RenderTickCounter tickCounter) {
		if (ModConfig.chiseledBookshelfPeeking) {
			MinecraftClient client = MinecraftClient.getInstance();
			int width = context.getScaledWindowWidth() / 2;
			int height = context.getScaledWindowHeight() / 2;
			windowHeightOffset = HudStatusBarHeightRegistry.getHeight(VanillaHudElements.AIR_BAR);
			if (!client.gameRenderer.getCamera().isThirdPerson() && client.crosshairTarget instanceof BlockHitResult result && client.world.getBlockEntity(result.getBlockPos()) instanceof ChiseledBookshelfBlockEntity chiseledBookshelfBlockEntity) {
				((ChiseledBookshelfBlock) Blocks.CHISELED_BOOKSHELF).getHitSlot(result, chiseledBookshelfBlockEntity.getCachedState().get(ChiseledBookshelfBlock.FACING)).ifPresent(index -> {
					List<ItemStack> stacks = ModBlockComponents.CHISELED_BOOKSHELF.get(chiseledBookshelfBlockEntity).getStacks();
					if (!stacks.isEmpty()) {
						ItemStack stack = stacks.get(index);
						if (!stack.isEmpty()) {
							EnchancementClient.drawTooltipsImmediately = true;
							context.drawItemTooltip(client.textRenderer, stack, width, height);
						}
					}
				});
			}
			windowHeightOffset = 0;
		}
	}
}
