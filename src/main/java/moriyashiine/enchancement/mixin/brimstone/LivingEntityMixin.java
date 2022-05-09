/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.brimstone;

import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "blockedByShield", at = @At("HEAD"), cancellable = true)
	private void enchancement$brimstone(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (source.getSource() instanceof BrimstoneEntity) {
			cir.setReturnValue(false);
		}
	}
}
