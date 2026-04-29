/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.gui.hud;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModBlockComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudStatusBarHeightRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.mixin.client.rendering.GuiAccessor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class ChiseledBookshelfPeekingHudElement implements HudElement {
	public static int windowHeightOffset = 0;

	@SuppressWarnings("ConstantValue")
	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		if (ModConfig.chiseledBookshelfPeeking) {
			Minecraft client = Minecraft.getInstance();
			if (((GuiAccessor) client.gui).fabric$callGetCameraPlayer() != null) {
				windowHeightOffset = HudStatusBarHeightRegistry.getHeight(VanillaHudElements.AIR_BAR);
			}
			int width = graphics.guiWidth() / 2;
			int height = graphics.guiHeight() / 2;
			if (!client.gameRenderer.getMainCamera().isDetached() && client.hitResult instanceof BlockHitResult result && client.level.getBlockEntity(result.getBlockPos()) instanceof ChiseledBookShelfBlockEntity chiseledBookShelfBlockEntity) {
				((ChiseledBookShelfBlock) Blocks.CHISELED_BOOKSHELF).getHitSlot(result, chiseledBookShelfBlockEntity.getBlockState().getValue(ChiseledBookShelfBlock.FACING)).ifPresent(slot -> {
					List<ItemStack> stacks = ModBlockComponents.CHISELED_BOOKSHELF.get(chiseledBookShelfBlockEntity).getStacks();
					if (!stacks.isEmpty()) {
						ItemStack stack = stacks.get(slot);
						if (!stack.isEmpty()) {
							graphics.setTooltipForNextFrame(client.font, stack, width, height);
							graphics.extractDeferredElements(0, 0, 0);
						}
					}
				});
			}
			windowHeightOffset = 0;
		}
	}
}
