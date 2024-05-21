/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.rebalancechanneling;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyReturnValue(method = "getFireAspect", at = @At("RETURN"))
	private static int enchancement$rebalanceChanneling(int original, LivingEntity entity) {
		if (ModConfig.rebalanceChanneling && original < 1 && EnchancementUtil.hasEnchantment(Enchantments.CHANNELING, entity.getMainHandStack())) {
			return 1;
		}
		return original;
	}
}
