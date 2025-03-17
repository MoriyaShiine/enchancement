/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.client.OutlineEntityEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class EntityXrayClientEvent implements OutlineEntityEvent.HasOutline {
	@Override
	public TriState hasOutline(Entity entity) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null && entity instanceof LivingEntity living && !living.isSneaking() && !living.isInvisible()) {
			float distance = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.ENTITY_XRAY, player, 0);
			if (distance > 0 && entity.distanceTo(player) < distance && !EnchancementUtil.hasAnyEnchantmentsWith(living, ModEnchantmentEffectComponentTypes.HIDE_LABEL_BEHIND_WALLS) && !living.canSee(player)) {
				return TriState.TRUE;
			}
		}
		return TriState.DEFAULT;
	}
}
