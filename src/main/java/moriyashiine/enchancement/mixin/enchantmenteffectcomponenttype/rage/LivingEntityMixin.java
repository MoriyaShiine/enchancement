/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage;

import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float enchancement$rageDealt(float value, ServerWorld serverWorld, DamageSource source) {
		if (source.getSource() instanceof LivingEntity living) {
			return value + RageEffect.getDamageDealtModifier(living, living.getMainHandStack());
		}
		return value;
	}

	@ModifyVariable(method = "modifyAppliedDamage", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getProtectionAmount(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/damage/DamageSource;)F"), argsOnly = true)
	private float enchancement$rageTaken(float value, DamageSource source) {
		if (!source.isIn(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
			value *= RageEffect.getDamageTakenModifier((LivingEntity) (Object) this);
		}
		return value;
	}
}
