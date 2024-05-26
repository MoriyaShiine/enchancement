/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.adrenaline;

import moriyashiine.enchancement.common.event.AdrenalineEvent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "modifyAppliedDamage", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getProtectionAmount(Ljava/lang/Iterable;Lnet/minecraft/entity/damage/DamageSource;)I"), argsOnly = true)
	private float enchancement$adrenaline(float value, DamageSource source) {
		int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.ADRENALINE, (LivingEntity) (Object) this);
		if (level > 0) {
			return value * AdrenalineEvent.getDamageMultiplier((LivingEntity) (Object) this, level);
		}
		return value;
	}
}
