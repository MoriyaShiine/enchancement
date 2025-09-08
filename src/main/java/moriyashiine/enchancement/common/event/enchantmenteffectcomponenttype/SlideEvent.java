/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.payload.StopSlidingC2SPayload;
import moriyashiine.strawberrylib.api.event.ModifyMovementEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class SlideEvent implements ModifyMovementEvents.JumpVelocity {
	@Override
	public Vec3d modify(Vec3d velocity, LivingEntity entity) {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(entity);
		if (slideComponent != null && slideComponent.isSliding()) {
			slideComponent.stopSliding();
			if (entity instanceof PlayerEntity player && player.getWorld().isClient) {
				StopSlidingC2SPayload.send();
			}
			return velocity.multiply(slideComponent.getJumpBonus(), slideComponent.getJumpBonus() > 2 ? 1.25 : 1, slideComponent.getJumpBonus());
		}
		return velocity;
	}
}
