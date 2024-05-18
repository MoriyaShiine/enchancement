/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.rebalancechanneling;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getFireAspect", at = @At("RETURN"), cancellable = true)
	private static void enchancement$rebalanceChanneling(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
		if (ModConfig.rebalanceChanneling && cir.getReturnValueI() < 1 && EnchancementUtil.hasEnchantment(Enchantments.CHANNELING, entity.getMainHandStack())) {
			cir.setReturnValue(1);
		}
	}
}
