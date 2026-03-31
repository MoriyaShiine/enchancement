/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.SummonEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SummonEntityEffect.class)
public class SummonEntityEffectMixin {
	@Inject(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantedItemInUse;owner()Lnet/minecraft/world/entity/LivingEntity;"))
	private void enchancement$rebalanceEnchantments(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position, CallbackInfo ci, @Local(name = "lightningBolt") LightningBolt lightningBolt) {
		if (ModConfig.rebalanceEnchantments) {
			ModEntityComponents.SAFE_LIGHTNING.get(lightningBolt).setSafe(true);
		}
	}
}
