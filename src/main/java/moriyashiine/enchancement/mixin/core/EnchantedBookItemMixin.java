package moriyashiine.enchancement.mixin.core;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnchantedBookItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {
	@Redirect(method = "appendStacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"))
	private int enchancement$singleLevelMode(Enchantment instance) {
		if (Enchancement.getConfig().singleLevelMode) {
			return 1;
		}
		return instance.getMaxLevel();
	}
}
