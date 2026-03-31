/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.delayedlaunch;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.entity.DelayedLaunchComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@ModifyExpressionValue(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 3))
	private boolean enchancement$delayedLaunch(boolean value, ServerLevel level, DamageSource source) {
		if (source.getDirectEntity() instanceof Projectile projectile) {
			@Nullable DelayedLaunchComponent delayedLaunchComponent = ModEntityComponents.DELAYED_LAUNCH.getNullable(projectile);
			if (delayedLaunchComponent != null && delayedLaunchComponent.alwaysHurt()) {
				return true;
			}
		}
		return value;
	}
}
