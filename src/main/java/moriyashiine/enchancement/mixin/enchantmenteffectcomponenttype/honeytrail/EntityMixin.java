/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.honeytrail;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.level.HoneyTrailComponent;
import moriyashiine.enchancement.common.init.ModLevelComponents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract Level level();

	@Shadow
	public abstract Vec3 position();

	@ModifyReturnValue(method = "getBlockSpeedFactor", at = @At("RETURN"))
	protected float enchancement$honeyTrail(float original) {
		if (inHoneySpot()) {
			return original * 0.4F;
		}
		return original;
	}

	@ModifyReturnValue(method = "getBlockJumpFactor", at = @At("RETURN"))
	protected float enchancement$honeyTrailJump(float original) {
		if (inHoneySpot()) {
			return original * 0.5F;
		}
		return original;
	}

	@SuppressWarnings("ConstantValue")
	@Unique
	private boolean inHoneySpot() {
		HoneyTrailComponent honeyTrailComponent = ModLevelComponents.HONEY_TRAIL.get(level());
		for (HoneyTrailComponent.HoneySpot spot : honeyTrailComponent.getHoneySpots()) {
			if (spot.getBox().contains(position()) && SLibUtils.shouldHurt(level().getEntity(spot.getOwnerId()), (Entity) (Object) this)) {
				return true;
			}
		}
		return false;
	}
}
