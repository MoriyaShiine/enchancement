package moriyashiine.enchancement.mixin.slide;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.registry.ModComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "applyMovementInput", at = @At("HEAD"), argsOnly = true)
	private float enchancement$slide(float slipperiness) {
		if (LivingEntity.class.cast(this) instanceof PlayerEntity player && player.isSneaking()) {
			SlideComponent slideComponent = ModComponents.SLIDE.get(player);
			if (slideComponent.shouldSlide()) {
				return slipperiness * MathHelper.lerp(slideComponent.getTicksSliding() / 60F, 0.45F, 1);
			}
		}
		return slipperiness;
	}

	@ModifyVariable(method = "jump", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
	private Vec3d enchancement$slide(Vec3d value) {
		if (LivingEntity.class.cast(this) instanceof PlayerEntity player) {
			SlideComponent slideComponent = ModComponents.SLIDE.get(player);
			if (slideComponent.shouldSlide()) {
				int ticksSliding = slideComponent.getTicksSliding();
				if (ticksSliding > 0) {
					slideComponent.setShouldSlide(false);
					slideComponent.setTicksSliding(60);
					return value.multiply(MathHelper.lerp(MathHelper.clamp((ticksSliding - 20) / 20F, 0, 1), 8, 1));
				}
			}
		}
		return value;
	}
}
