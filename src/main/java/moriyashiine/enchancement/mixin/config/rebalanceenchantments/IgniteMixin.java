/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.config.IgnitedComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.Ignite;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ignite.class)
public class IgniteMixin {
	@Inject(method = "apply", at = @At("HEAD"))
	private void enchancement$rebalanceEnchantments(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position, CallbackInfo ci) {
		if (ModConfig.rebalanceEnchantments) {
			ModEntityComponents.IGNITED.maybeGet(entity).ifPresent(IgnitedComponent::markIgnited);
		}
	}

	@ModifyArg(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;igniteForSeconds(F)V"))
	private float enchancement$rebalanceEnchantments(float value) {
		if (ModConfig.rebalanceEnchantments) {
			return value * 3 / 4;
		}
		return value;
	}
}
