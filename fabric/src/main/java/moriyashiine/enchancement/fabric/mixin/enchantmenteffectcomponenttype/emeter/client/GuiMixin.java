package moriyashiine.enchancement.fabric.mixin.enchantmenteffectcomponenttype.emeter.client;

import moriyashiine.enchancement.client.gui.hud.EMeterHudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Gui.class)
public class GuiMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@ModifyArg(method = "extractSelectedItemName", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;textWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIII)V"), index = 3)
	private int enchancement$eMeter(int textY) {
		if (EMeterHudElement.shouldMove(minecraft)) {
			textY -= EMeterHudElement.yOffset;
		}
		return textY;
	}
}
