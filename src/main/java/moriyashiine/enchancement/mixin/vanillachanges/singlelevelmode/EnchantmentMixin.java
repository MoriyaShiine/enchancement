/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.singlelevelmode;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	@Unique
	private static boolean disableRecursion = false;

	@Shadow
	public abstract int getMaxLevel();

	@Inject(method = "getMaxLevel", at = @At("HEAD"), cancellable = true)
	private void enchancement$singleLevelMode(CallbackInfoReturnable<Integer> cir) {
		if (ModConfig.singleLevelMode && !disableRecursion) {
			if (!EnchancementUtil.ORIGINAL_MAX_LEVELS.containsKey((Enchantment) (Object) this)) {
				disableRecursion = true;
				EnchancementUtil.ORIGINAL_MAX_LEVELS.put((Enchantment) (Object) this, getMaxLevel());
				disableRecursion = false;
			}
			cir.setReturnValue(1);
		}
	}
}
