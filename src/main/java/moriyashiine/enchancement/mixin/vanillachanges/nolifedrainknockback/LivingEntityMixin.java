/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.nolifedrainknockback;

import moriyashiine.enchancement.common.init.ModDamageTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Unique
	private static boolean cancelKnockback = false;

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V", shift = At.Shift.BEFORE))
	private void enchancement$cancelLifeDrainKnockbackBefore(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (source.isOf(ModDamageTypes.LIFE_DRAIN)) {
			cancelKnockback = true;
		}
	}

	@Inject(method = "takeKnockback", at = @At("HEAD"), cancellable = true)
	private void enchancement$cancelLifeDrainKnockback(double strength, double x, double z, CallbackInfo ci) {
		if (cancelKnockback) {
			cancelKnockback = false;
			ci.cancel();
		}
	}
}
