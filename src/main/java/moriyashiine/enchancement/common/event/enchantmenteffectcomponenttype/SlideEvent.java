/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.payload.StopSlidingC2SPayload;
import moriyashiine.strawberrylib.api.event.ModifyMovementEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SlideEvent implements ModifyMovementEvents.JumpDelta {
	@Override
	public Vec3 modify(Vec3 delta, LivingEntity entity) {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(entity);
		if (slideComponent != null && slideComponent.isSliding()) {
			slideComponent.stopSliding();
			if (entity instanceof Player player && player.level().isClientSide()) {
				StopSlidingC2SPayload.send();
			}
			return delta.multiply(slideComponent.getJumpBonus(), slideComponent.getJumpBonus() > 2 ? 1.25 : 1, slideComponent.getJumpBonus());
		}
		return delta;
	}
}
