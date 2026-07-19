package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.client.AddNightVisionScaleEvent;
import net.minecraft.world.entity.LivingEntity;

public class NightVisionClientEvent implements AddNightVisionScaleEvent {
	public static void init() {
		AddNightVisionScaleEvent.EVENT.register(new NightVisionClientEvent());
	}

	@Override
	public float addScale(LivingEntity entity) {
		return entity.isSpectator() ? 0 : EnchancementUtil.getValue(EnchancementEnchantmentEffectComponentTypes.NIGHT_VISION, entity, 0);
	}
}
