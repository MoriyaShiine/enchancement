package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.RotationBurstComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.world.item.effects.RotationBurstEffect;
import moriyashiine.strawberrylib.api.event.ModifyMovementEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class RotationBurstEvent implements ModifyMovementEvents.JumpDelta {
	public static void init() {
		ModifyMovementEvents.JUMP_DELTA.register(new RotationBurstEvent());
	}

	@Override
	public Vec3 modify(Vec3 delta, LivingEntity entity) {
		RotationBurstComponent rotationBurst = EnchancementEntityComponents.ROTATION_BURST.getNullable(entity);
		if (rotationBurst != null && rotationBurst.shouldWavedash()) {
			rotationBurst.setCooldown(0);
			return delta.scale(RotationBurstEffect.getWavedashStrength(entity));
		}
		return delta;
	}
}
