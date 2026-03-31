/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalancestatuseffects;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MobEffect.class)
public class MobEffectMixin {
	@Unique
	private static final Identifier STRENGTH_ID = Identifier.withDefaultNamespace("effect.strength");
	@Unique
	private static final Identifier WEAKNESS_ID = Identifier.withDefaultNamespace("effect.weakness");

	@ModifyVariable(method = "addAttributeModifier", at = @At("HEAD"), argsOnly = true)
	private double enchancement$rebalanceStatusEffects(double amount, Holder<Attribute> attribute, Identifier id) {
		if (ModConfig.rebalanceStatusEffects && attribute == Attributes.ATTACK_DAMAGE) {
			if (id.equals(STRENGTH_ID)) {
				return 1;
			} else if (id.equals(WEAKNESS_ID)) {
				return -1;
			}
		}
		return amount;
	}
}
