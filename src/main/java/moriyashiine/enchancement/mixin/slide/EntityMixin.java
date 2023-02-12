package moriyashiine.enchancement.mixin.slide;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyVariable(method = "getJumpVelocityMultiplier", at = @At("STORE"), ordinal = 0)
	private float enchancement$slide(float value) {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
		if (slideComponent != null) {
			if (slideComponent.getJumpBonus() > 2) {
				return value * 1.25F;
			} else if (slideComponent.shouldBoostJump()) {
				return value * 2;
			}
		}
		return value;
	}

	@Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
	private void enchancement$slide(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && damageSource == DamageSource.IN_WALL) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
			if (slideComponent != null && slideComponent.isSliding()) {
				cir.setReturnValue(true);
			}
		}
	}
}
