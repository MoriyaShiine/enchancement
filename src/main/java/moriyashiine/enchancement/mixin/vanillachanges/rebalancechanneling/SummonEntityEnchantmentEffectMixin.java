/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.rebalancechanneling;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.entity.SummonEntityEnchantmentEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SummonEntityEnchantmentEffect.class)
public class SummonEntityEnchantmentEffectMixin {
	@Inject(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentEffectContext;owner()Lnet/minecraft/entity/LivingEntity;"))
	private void enchancement$rebalanceChanneling(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos, CallbackInfo ci, @Local LightningEntity lightning) {
		if (ModConfig.rebalanceChanneling) {
			ModEntityComponents.SAFE_LIGHTNING.get(lightning).setSafe(true);
		}
	}
}
