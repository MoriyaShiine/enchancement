package moriyashiine.enchancement.mixin.vanillachanges;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
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
	@Inject(method = "getLevel", at = @At("RETURN"), cancellable = true)
	private static void enchancement$singleLevelMode(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (cir.getReturnValueI() > 0 && Enchancement.getConfig().singleLevelMode) {
			cir.setReturnValue(enchantment.getMaxLevel());
		}
	}

	@Inject(method = "getLoyalty", at = @At("HEAD"), cancellable = true)
	private static void enchancement$giveAllTridentsLoyalty(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (Enchancement.getConfig().allTridentsHaveLoyalty && stack.getItem() instanceof TridentItem) {
			cir.setReturnValue(Enchantments.LOYALTY.getMaxLevel());
		}
	}

	@Inject(method = "getLure", at = @At("HEAD"), cancellable = true)
	private static void enchancement$luckOfTheSeaWithLure(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (Enchancement.getConfig().luckOfTheSeaHasLure) {
			int level = EnchantmentHelper.getLuckOfTheSea(stack);
			if (level > 0) {
				cir.setReturnValue(Enchantments.LURE.getMaxLevel());
			}
		}
	}

	@ModifyExpressionValue(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"))
	private static int enchancement$singleLevelMode(int value) {
		if (Enchancement.getConfig().singleLevelMode) {
			return 1;
		}
		return value;
	}
}
