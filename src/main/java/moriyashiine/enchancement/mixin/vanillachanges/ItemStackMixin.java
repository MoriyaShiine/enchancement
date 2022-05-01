package moriyashiine.enchancement.mixin.vanillachanges;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean hasEnchantments();

	@ModifyVariable(method = "addEnchantment", at = @At("HEAD"), argsOnly = true)
	private int enchancement$singleLevelMode(int value) {
		if (Enchancement.getConfig().singleLevelMode) {
			return 1;
		}
		return value;
	}

	@Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
	private void enchancement$singleEnchantmentMode(Enchantment enchantment, int level, CallbackInfo ci) {
		if (Registry.ENCHANTMENT.getId(enchantment) == null || (Enchancement.getConfig().singleEnchantmentMode && hasEnchantments())) {
			ci.cancel();
		}
	}

	@Inject(method = "isDamageable", at = @At("HEAD"), cancellable = true)
	private void enchancement$unbreakingChanges(CallbackInfoReturnable<Boolean> cir) {
		if (EnchancementUtil.shouldBeUnbreakable(ItemStack.class.cast(this))) {
			cir.setReturnValue(false);
		}
	}
}
