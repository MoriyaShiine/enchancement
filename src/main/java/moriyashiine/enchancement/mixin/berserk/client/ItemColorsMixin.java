package moriyashiine.enchancement.mixin.berserk.client;

import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ItemColors.class)
public class ItemColorsMixin {
	@Inject(method = "getColor", at = @At("RETURN"), cancellable = true)
	private void enchancement$berserk(ItemStack item, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		int color = EnchancementClientUtil.berserkColor;
		if (color != -1) {
			int original = cir.getReturnValueI();
			if (original != -1) {
				color *= original;
			}
			cir.setReturnValue(color);
		}
	}
}
