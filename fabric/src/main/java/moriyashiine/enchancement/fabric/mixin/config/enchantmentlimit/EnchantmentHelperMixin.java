package moriyashiine.enchancement.fabric.mixin.config.enchantmentlimit;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyExpressionValue(method = "lambda$getAvailableEnchantmentResults$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMinCost(I)I"))
	private static int enchancement$enchantmentLimitMin(int original) {
		if (EnchancementConfig.enchantmentLimit > 0) {
			return 0;
		}
		return original;
	}

	@ModifyExpressionValue(method = "lambda$getAvailableEnchantmentResults$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxCost(I)I"))
	private static int enchancement$enchantmentLimitMax(int original) {
		if (EnchancementConfig.enchantmentLimit > 0) {
			return Integer.MAX_VALUE;
		}
		return original;
	}
}
