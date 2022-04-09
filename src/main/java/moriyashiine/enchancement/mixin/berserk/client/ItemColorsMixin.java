package moriyashiine.enchancement.mixin.berserk.client;

import me.shedaniel.math.Color;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
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
		float damageBonus = EnchancementUtil.getBonusBerserkDamage(MinecraftClient.getInstance().player, item);
		if (damageBonus > 0) {
			float other = 1 - damageBonus / EnchancementUtil.getMaxBonusBerserkDamage(item);
			int color = Color.ofRGB(1F, other, other).getColor();
			int original = cir.getReturnValueI();
			if (original != -1) {
				color *= original;
			}
			cir.setReturnValue(color);
		}
	}
}
