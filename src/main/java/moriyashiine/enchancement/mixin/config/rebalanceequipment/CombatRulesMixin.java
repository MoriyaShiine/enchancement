/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CombatRules.class)
public class CombatRulesMixin {
	@ModifyVariable(method = "getDamageAfterAbsorb", at = @At(value = "HEAD"), argsOnly = true, ordinal = 0)
	private static float enchancement$rebalanceEquipment(float damage, LivingEntity victim) {
		if (ModConfig.rebalanceEquipment && damage > 1) {
			double[] enchantedArmor = {0};
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.getType() != EquipmentSlot.Type.HAND) {
					ItemStack stack = victim.getItemBySlot(slot);
					if (stack.isEnchanted()) {
						stack.forEachModifier(slot, (entry, modifier) -> {
							if (entry == Attributes.ARMOR && modifier.operation() == AttributeModifier.Operation.ADD_VALUE) {
								enchantedArmor[0] += modifier.amount();
							}
						});
					}
				}
			}
			double reduction = 1 - 0.075F * Math.min(20, enchantedArmor[0]) / 20;
			return (float) Math.pow(damage, reduction);
		}
		return damage;
	}
}
