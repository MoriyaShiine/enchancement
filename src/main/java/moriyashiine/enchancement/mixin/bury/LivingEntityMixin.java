package moriyashiine.enchancement.mixin.bury;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "damage", at = @At("RETURN"))
	private void enchancement$bury(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ()) {
			ModEntityComponents.BURY.maybeGet(this).ifPresent(buryComponent -> {
				if (buryComponent.getBuryPos() != null) {
					buryComponent.unbury();
				}
			});
		}
	}
}
