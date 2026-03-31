/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.client.OutlineEntityEvent;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.OptionalInt;

public class EntityXrayClientEvent {
	private static final Minecraft client = Minecraft.getInstance();

	private static float xrayDistance = 0;

	public static class Tick implements ClientTickEvents.EndLevelTick {
		@Override
		public void onEndTick(ClientLevel level) {
			xrayDistance = client.player != null && !client.player.isSpectator() ? EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.ENTITY_XRAY, client.player, 0) : 0;
		}
	}

	public static class Outline implements OutlineEntityEvent {
		private static final OutlineData DATA = new OutlineData(TriState.TRUE, OptionalInt.empty());

		@Override
		public OutlineData getOutlineData(Entity entity) {
			if (xrayDistance > 0) {
				Player player = client.player;
				if (player != null && entity instanceof LivingEntity living && !living.isShiftKeyDown() && !living.isInvisible()) {
					if (entity.distanceTo(player) < xrayDistance && !EnchancementUtil.hasAnyEnchantmentsWith(living, ModEnchantmentEffectComponentTypes.HIDE_LABEL_BEHIND_WALLS) && !living.hasLineOfSight(player)) {
						return DATA;
					}
				}
			}
			return null;
		}
	}
}
