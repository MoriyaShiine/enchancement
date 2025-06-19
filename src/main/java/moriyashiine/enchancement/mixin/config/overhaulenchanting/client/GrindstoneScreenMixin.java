/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchanting.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GrindstoneScreen.class)
public abstract class GrindstoneScreenMixin extends HandledScreen<GrindstoneScreenHandler> {
	@Unique
	private static final Identifier BOOK_OUTLINE = Enchancement.id("container/grindstone/book");

	public GrindstoneScreenMixin(GrindstoneScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@WrapOperation(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIII)V"))
	private void enchancement$overhaulEnchanting(DrawContext instance, RenderPipeline pipeline, Identifier sprite, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, Operation<Void> original) {
		original.call(instance, pipeline, sprite, x, y, u, v, width, height, textureWidth, textureHeight);
		if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && handler.getSlot(0).getStack().hasEnchantments()) {
			instance.drawGuiTexture(pipeline, BOOK_OUTLINE, x + 49, y + 40, 16, 16);
		}
	}
}
