package moriyashiine.enchancement.mixin.vanillachanges.singlelevelmode;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {
	@ModifyVariable(method = "addEnchantment", at = @At("HEAD"), argsOnly = true)
	private static EnchantmentLevelEntry enchancement$singleLevelMode(EnchantmentLevelEntry value) {
		if (Enchancement.getConfig().singleLevelMode) {
			return new EnchantmentLevelEntry(value.enchantment, 1);
		}
		return value;
	}

	@ModifyExpressionValue(method = "appendStacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"))
	private int enchancement$singleLevelMode(int value) {
		if (Enchancement.getConfig().singleLevelMode) {
			return 1;
		}
		return value;
	}
}
