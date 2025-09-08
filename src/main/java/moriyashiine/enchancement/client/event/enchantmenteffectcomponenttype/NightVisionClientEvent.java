/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.client.ModifyNightVisionStrengthEvent;
import net.minecraft.entity.LivingEntity;

public class NightVisionClientEvent implements ModifyNightVisionStrengthEvent {
	@Override
	public float modify(float strength, LivingEntity entity) {
		return entity.isSpectator() ? 0 : EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.NIGHT_VISION, entity, 0);
	}
}
