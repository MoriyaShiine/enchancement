package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.predicates.DamagePredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.PlayerHurtEntityTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
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
	private static void enchancement$rebalanceEquipment(HolderLookup.Provider registries, Holder.Reference<Advancement> advancement, CallbackInfo ci) {
		if (EnchancementConfig.rebalanceEquipment && advancement.key().identifier().equals(OVEROVERKILL)) {
			for (Criterion<?> criterion : advancement.value().criteria().values()) {
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
