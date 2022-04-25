package moriyashiine.enchancement.mixin.vanillachanges;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Inject(method = "canCombine", at = @At("HEAD"), cancellable = true)
	private void enchancement$singleEnchantmentMode(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
		if (Enchancement.getConfig().singleEnchantmentMode) {
			cir.setReturnValue(false);
		}
	}

	@ModifyExpressionValue(method = "getName", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"))
	private int enchancement$singleLevelMode(int value) {
		if (Enchancement.getConfig().singleLevelMode) {
			return 1;
		}
		return value;
	}

	@SuppressWarnings("ConstantConditions")
	@Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
	private void enchancement$allowInfinityOnCrossbows(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (Enchancement.getConfig().allowInfinityOnCrossbows && (Object) this == Enchantments.INFINITY && stack.getItem() instanceof CrossbowItem) {
			cir.setReturnValue(true);
		}
	}
}
