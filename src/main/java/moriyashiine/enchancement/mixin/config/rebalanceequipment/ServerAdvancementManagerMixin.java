/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.DamagePredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.PlayerHurtEntityTrigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerAdvancementManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {
	@Unique
	private static final Identifier OVEROVERKILL = Identifier.withDefaultNamespace("adventure/overoverkill");

	@Inject(method = "validate", at = @At("HEAD"))
	private void enchancement$rebalanceEquipment(Identifier id, Advancement advancement, CallbackInfo ci) {
		if (ModConfig.rebalanceEquipment && id.equals(OVEROVERKILL)) {
			for (Criterion<?> criterion : advancement.criteria().values()) {
				if (criterion.trigger() == CriteriaTriggers.PLAYER_HURT_ENTITY) {
					PlayerHurtEntityTrigger.TriggerInstance triggerInstance = ((PlayerHurtEntityTrigger.TriggerInstance) criterion.triggerInstance());
					DamagePredicate damagePredicate = triggerInstance.damage().orElse(null);
					if (damagePredicate == null) {
						continue;
					}
					damagePredicate.dealtDamage = MinMaxBounds.Doubles.atLeast(50);
				}
			}
		}
	}
}
