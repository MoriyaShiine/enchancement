/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.channelingignitesonmelee;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Inject(method = "onTargetDamaged", at = @At("HEAD"))
	private void enchancement$channelingIgnitesOnMelee(LivingEntity user, Entity target, int level, CallbackInfo ci) {
		if (ModConfig.channelingIgnitesOnMelee && EnchancementUtil.hasEnchantment(Enchantments.CHANNELING, user.getMainHandStack())) {
			target.setOnFireFor(3);
		}
	}
}
