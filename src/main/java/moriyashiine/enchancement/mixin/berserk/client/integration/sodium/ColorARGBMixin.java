/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.berserk.client.integration.sodium;

import me.jellysquid.mods.sodium.client.util.color.ColorARGB;
import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(value = ColorARGB.class, remap = false)
public class ColorARGBMixin {
	@Inject(method = "toABGR(II)I", at = @At("HEAD"), cancellable = true)
	private static void enchancement$berserk(int color, int alpha, CallbackInfoReturnable<Integer> cir) {
		int berserkColor = EnchancementClientUtil.berserkColor;
		if (berserkColor != -1) {
			if (color != -1) {
				berserkColor *= color;
			}
			cir.setReturnValue(Integer.reverseBytes(berserkColor << 8 | alpha));
		}
	}
}
