/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DamageUtil.class)
public class DamageUtilMixin {
	@ModifyVariable(method = "getDamageLeft", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
	private static float enchancement$rebalanceEquipment(float damageAmount, LivingEntity armorWearer) {
		if (ModConfig.rebalanceEquipment && damageAmount > 1) {
			double[] enchantedArmor = {0};
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.isArmorSlot()) {
					ItemStack stack = armorWearer.getEquippedStack(slot);
					if (stack.hasEnchantments()) {
						stack.applyAttributeModifiers(slot, (entry, modifier) -> {
							if (entry == EntityAttributes.ARMOR && modifier.operation() == EntityAttributeModifier.Operation.ADD_VALUE) {
								enchantedArmor[0] += modifier.value();
							}
						});
					}
				}
			}
			double reduction = 1 - 0.075F * Math.min(20, enchantedArmor[0]) / 20;
			return (float) Math.pow(damageAmount, reduction);
		}
		return damageAmount;
	}
}
