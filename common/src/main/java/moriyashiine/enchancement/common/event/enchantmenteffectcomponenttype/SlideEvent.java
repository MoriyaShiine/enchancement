package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.SlideComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.payload.SlideC2SPayload;
import moriyashiine.strawberrylib.api.event.ModifyMovementEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SlideEvent implements ModifyMovementEvents.JumpDelta {
	public static void init() {
		ModifyMovementEvents.JUMP_DELTA.register(new SlideEvent());
	}

	@Override
	public Vec3 modify(Vec3 delta, LivingEntity entity) {
		SlideComponent slide = EnchancementEntityComponents.SLIDE.getNullable(entity);
		if (slide != null && slide.isSliding()) {
			slide.stopSliding();
			if (entity instanceof Player player && player.level().isClientSide()) {
				SlideC2SPayload.sendStop(entity);
			}
			return delta.multiply(slide.getJumpBonus(), slide.getJumpBonus() > 2 ? 1.25 : 1, slide.getJumpBonus());
		}
		return delta;
	}
}
