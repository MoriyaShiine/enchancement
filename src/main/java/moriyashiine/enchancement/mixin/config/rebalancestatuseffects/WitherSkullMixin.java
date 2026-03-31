/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalancestatuseffects;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WitherSkull.class)
public class WitherSkullMixin extends AbstractHurtingProjectile {
	protected WitherSkullMixin(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
		super(type, level);
	}

	@ModifyArg(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;<init>(Lnet/minecraft/core/Holder;II)V"), index = 1)
	private int enchancement$rebalanceStatusEffects(int duration) {
		if (ModConfig.rebalanceStatusEffects) {
			return duration / (level().getDifficulty() == Difficulty.HARD ? 4 : 2);
		}
		return duration;
	}
}
