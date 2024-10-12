/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.PlayerHurtEntityCriterion;
import net.minecraft.predicate.DamagePredicate;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerAdvancementLoader.class)
public class ServerAdvancementLoaderMixin {
	@Unique
	private static final Identifier OVEROVERKILL = Identifier.ofVanilla("adventure/overoverkill");

	@Inject(method = "validate", at = @At("HEAD"))
	private void enchancement$rebalanceEquipment(Identifier id, Advancement advancement, CallbackInfo ci) {
		if (ModConfig.rebalanceEquipment && id.equals(OVEROVERKILL)) {
			for (AdvancementCriterion<?> value : advancement.criteria().values()) {
				if (value.trigger() == Criteria.PLAYER_HURT_ENTITY) {
					PlayerHurtEntityCriterion.Conditions conditions = ((PlayerHurtEntityCriterion.Conditions) value.conditions());
					DamagePredicate damagePredicate = conditions.damage().orElse(null);
					if (damagePredicate == null) {
						continue;
					}
					damagePredicate.dealt = NumberRange.DoubleRange.atLeast(20);
				}
			}
		}
	}
}
