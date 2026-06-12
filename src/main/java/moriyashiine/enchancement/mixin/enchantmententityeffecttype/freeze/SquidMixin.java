/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze;

import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Squid.class)
public abstract class SquidMixin extends AgeableWaterCreature {
	@Shadow
	private float tentacleSpeed;

	protected SquidMixin(EntityType<? extends AgeableWaterCreature> type, Level level) {
		super(type, level);
	}

	@Inject(method = "aiStep", at = @At("HEAD"))
	private void enchancement$freeze(CallbackInfo ci) {
		if (EnchancementEntityComponents.FROZEN.get(this).isFrozen()) {
			tentacleSpeed = 0;
		}
	}

	@ModifyArg(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/squid/Squid;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 0))
	private Vec3 enchancement$freeze(Vec3 delta) {
		if (EnchancementEntityComponents.FROZEN.get(this).isFrozen()) {
			return getDeltaMovement();
		}
		return delta;
	}
}
