/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalancestatuseffects;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(StatusEffect.class)
public class StatusEffectMixin {
	@Unique
	private static final Identifier STRENGTH_ID = Identifier.ofVanilla("effect.strength");
	@Unique
	private static final Identifier WEAKNESS_ID = Identifier.ofVanilla("effect.weakness");

	@ModifyVariable(method = "addAttributeModifier", at = @At("HEAD"), argsOnly = true)
	private double enchancement$rebalanceStatusEffects(double value, RegistryEntry<EntityAttribute> attribute, Identifier id) {
		if (ModConfig.rebalanceStatusEffects && attribute == EntityAttributes.ATTACK_DAMAGE) {
			if (id.equals(STRENGTH_ID)) {
				return 1;
			} else if (id.equals(WEAKNESS_ID)) {
				return -1;
			}
		}
		return value;
	}
}
