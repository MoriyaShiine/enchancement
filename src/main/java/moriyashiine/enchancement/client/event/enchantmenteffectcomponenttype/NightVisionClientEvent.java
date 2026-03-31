/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.client.AddNightVisionScaleEvent;
import net.minecraft.world.entity.LivingEntity;

public class NightVisionClientEvent implements AddNightVisionScaleEvent {
	@Override
	public float addScale(LivingEntity entity) {
		return entity.isSpectator() ? 0 : EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.NIGHT_VISION, entity, 0);
	}
}
