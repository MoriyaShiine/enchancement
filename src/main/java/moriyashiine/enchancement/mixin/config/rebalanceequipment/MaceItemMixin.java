/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.MaceItem;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MaceItem.class)
public class MaceItemMixin {
	@ModifyArg(method = "createAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/EntityAttributeModifier;<init>(Lnet/minecraft/util/Identifier;DLnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;)V", ordinal = 1), index = 1)
	private static double enchancement$rebalanceEquipment(double value) {
		if (ModConfig.rebalanceEquipment) {
			return -3.2;
		}
		return value;
	}

	@ModifyReturnValue(method = "getBonusAttackDamage", at = @At(value = "RETURN", ordinal = 2))
	private float enchancement$rebalanceEquipment(float original, Entity target, float baseAttackDamage, DamageSource damageSource, @Local LivingEntity living) {
		if (ModConfig.rebalanceEquipment) {
			float damage = (float) (6 * Math.log(living.fallDistance + 1));
			if (target.getWorld() instanceof ServerWorld serverWorld) {
				float bonus = EnchantmentHelper.getSmashDamagePerFallenBlock(serverWorld, living.getWeaponStack(), target, damageSource, 0);
				return damage + bonus * 2;
			}
			return damage;
		}
		return original;
	}
}
