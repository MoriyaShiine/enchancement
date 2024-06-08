/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StepHeightEvent implements ServerTickEvents.EndTick {
	public static final Map<Object, LivingEntity> ENTITIES = new HashMap<>();

	private static final EntityAttributeModifier STEP_HEIGHT_MODIFIER = new EntityAttributeModifier(UUID.fromString("ffa8a401-83c0-46a2-8510-66a66aed2275"), "Enchantment modifier", 1, EntityAttributeModifier.Operation.ADD_VALUE);

	@Override
	public void onEndTick(MinecraftServer server) {
		PlayerLookup.all(server).forEach(foundPlayer -> {
			EntityAttributeInstance stepHeightAttribute = foundPlayer.getAttributeInstance(EntityAttributes.GENERIC_STEP_HEIGHT);
			boolean allowed = false;
			for (LivingEntity entity : ENTITIES.values()) {
				if (foundPlayer == entity) {
					allowed = true;
					break;
				}
			}
			if (allowed) {
				if (!stepHeightAttribute.hasModifier(STEP_HEIGHT_MODIFIER)) {
					stepHeightAttribute.addPersistentModifier(STEP_HEIGHT_MODIFIER);
				}
			} else if (stepHeightAttribute.hasModifier(STEP_HEIGHT_MODIFIER)) {
				stepHeightAttribute.removeModifier(STEP_HEIGHT_MODIFIER);
			}
		});
	}
}
