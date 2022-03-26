package moriyashiine.enchancement.mixin.vanillachanges;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getLoyalty", at = @At("HEAD"), cancellable = true)
	private static void enchancement$giveAllTridentsLoyalty(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (Enchancement.getConfig().allTridentsHaveLoyalty && stack.getItem() instanceof TridentItem) {
			cir.setReturnValue(Enchancement.CACHED_MAX_LEVELS.get(Enchantments.LOYALTY));
		}
	}

	@Inject(method = "getLure", at = @At("HEAD"), cancellable = true)
	private static void enchancement$luckOfTheSeaWithLure(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (Enchancement.getConfig().luckOfTheSeaHasLure) {
			int level = EnchantmentHelper.getLuckOfTheSea(stack);
			if (level > 0) {
				cir.setReturnValue(Enchancement.CACHED_MAX_LEVELS.get(Enchantments.LURE));
			}
		}
	}
}
