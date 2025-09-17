package moriyashiine.enchancement.mixin.config.overhaulenchanting.client;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.gui.tooltip.StoredEnchantmentsTooltipComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DrawContext.class)
public class DrawContextMixin {
	@Inject(method = "drawTooltipImmediately", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/tooltip/TooltipComponent;getWidth(Lnet/minecraft/client/font/TextRenderer;)I"))
	private void enchancement$overhaulEnchanting(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, @Nullable Identifier texture, CallbackInfo ci, @Local TooltipComponent component) {
		if (component instanceof StoredEnchantmentsTooltipComponent storedEnchantmentsComponent) {
			storedEnchantmentsComponent.cacheDimensions(textRenderer, x, y, (DrawContext) (Object) this, positioner);
		}
	}
}
