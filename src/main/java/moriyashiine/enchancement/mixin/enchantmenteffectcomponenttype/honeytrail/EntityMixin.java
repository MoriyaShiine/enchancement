/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.honeytrail;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.world.HoneyTrailComponent;
import moriyashiine.enchancement.common.init.ModWorldComponents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract World getWorld();

	@Shadow
	public abstract Vec3d getPos();

	@ModifyReturnValue(method = "getVelocityMultiplier", at = @At("RETURN"))
	protected float enchancement$honeyTrail(float original) {
		if (inHoneySpot()) {
			return original * 0.4F;
		}
		return original;
	}

	@ModifyReturnValue(method = "getJumpVelocityMultiplier", at = @At("RETURN"))
	protected float enchancement$honeyTrailJump(float original) {
		if (inHoneySpot()) {
			return original * 0.5F;
		}
		return original;
	}

	@Unique
	private boolean inHoneySpot() {
		HoneyTrailComponent honeyTrailComponent = ModWorldComponents.HONEY_TRAIL.get(getWorld());
		for (HoneyTrailComponent.HoneySpot spot : honeyTrailComponent.getHoneySpots()) {
			if (spot.getBox().contains(getPos()) && SLibUtils.shouldHurt(getWorld().getEntityLookup().get(spot.getOwnerId()), (Entity) (Object) this)) {
				return true;
			}
		}
		return false;
	}
}
