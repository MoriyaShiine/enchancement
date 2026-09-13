package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.api.event.CappedMultiplyDeltaMovementEvent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class GlideEvent implements CappedMultiplyDeltaMovementEvent {
	public static void init() {
		CappedMultiplyDeltaMovementEvent.EVENT.register(new GlideEvent());
	}

	@Override
	public float multiply(Level level, LivingEntity living) {
		if (EnchancementEntityComponents.GLIDE.get(living).isGliding()) {
			return 1.3F;
		}
		return 1;
	}
}
