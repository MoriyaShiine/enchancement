package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.emeter.client;

import moriyashiine.enchancement.client.gui.hud.EMeterHudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@Inject(method = "extractArmor", at = @At("HEAD"))
	private static void enchancement$eMeter(GuiGraphicsExtractor graphics, Player player, int yLineBase, int numHealthRows, int healthRowHeight, int xLeft, CallbackInfo ci) {
		EMeterHudElement.yOffset = EMeterHudElement.getYOffset(numHealthRows, healthRowHeight);
	}

	@ModifyArg(method = "extractOverlayMessage", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;translate(FF)Lorg/joml/Matrix3x2f;"), index = 1)
	private float enchancement$eMeter(float y) {
		if (EMeterHudElement.shouldMove(minecraft)) {
			y -= EMeterHudElement.yOffset;
		}
		return y;
	}
}
