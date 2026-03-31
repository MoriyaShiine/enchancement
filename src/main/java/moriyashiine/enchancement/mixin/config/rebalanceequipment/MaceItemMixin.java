/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MaceItem.class)
public class MaceItemMixin {
	@ModifyArg(method = "createAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;<init>(Lnet/minecraft/resources/Identifier;DLnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;)V", ordinal = 1), index = 1)
	private static double enchancement$rebalanceEquipment(double value) {
		if (ModConfig.rebalanceEquipment) {
			return -3.2;
		}
		return value;
	}

	@ModifyReturnValue(method = "getAttackDamageBonus", at = @At(value = "RETURN", ordinal = 2))
	private float enchancement$rebalanceEquipment(float original, Entity target, float baseAttackDamage, DamageSource damageSource, @Local LivingEntity living) {
		if (ModConfig.rebalanceEquipment) {
			float damage = (float) EnchancementUtil.altLog(2.25, living.fallDistance, 6);
			if (target.level() instanceof ServerLevel level) {
				float bonus = EnchantmentHelper.modifyFallBasedDamage(level, living.getWeaponItem(), target, damageSource, 0);
				return damage + bonus * 2;
			}
			return damage;
		}
		return original;
	}
}
