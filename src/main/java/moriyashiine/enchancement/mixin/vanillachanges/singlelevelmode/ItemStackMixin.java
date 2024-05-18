/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.singlelevelmode;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean hasEnchantments();

	@ModifyVariable(method = "addEnchantment", at = @At("HEAD"), argsOnly = true)
	private int enchancement$singleLevelMode(int value) {
		if (ModConfig.singleLevelMode) {
			return 1;
		}
		return value;
	}

	@Inject(method = "getRarity", at = @At("RETURN"), cancellable = true)
	private void enchancement$singleLevelMode(CallbackInfoReturnable<Rarity> cir) {
		if (ModConfig.singleLevelMode && hasEnchantments() && !EnchancementUtil.hasWeakEnchantments((ItemStack) (Object) this)) {
			cir.setReturnValue(Rarity.values()[Math.min(cir.getReturnValue().ordinal() + 1, Rarity.values().length - 1)]);
		}
	}
}
