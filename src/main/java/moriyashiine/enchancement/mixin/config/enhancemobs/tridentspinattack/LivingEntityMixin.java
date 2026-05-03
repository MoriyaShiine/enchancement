/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enhancemobs.tridentspinattack;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract void swing(InteractionHand hand);

	@Shadow
	public abstract boolean doHurtTarget(ServerLevel level, Entity target);

	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Inject(method = "doAutoAttackOnTouch", at = @At("HEAD"))
	private void enchancement$enhanceMobs(LivingEntity entity, CallbackInfo ci) {
		if (ModConfig.enhanceMobs && level() instanceof ServerLevel level) {
			swing(InteractionHand.MAIN_HAND);
			doHurtTarget(level, entity);
		}
	}
}
