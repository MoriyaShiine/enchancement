package moriyashiine.enchancement.mixin.slide;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "applyMovementInput", at = @At("HEAD"), argsOnly = true)
	private float enchancement$slide(float slipperiness) {
		if (isSneaking()) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
			if (slideComponent != null && slideComponent.shouldSlide()) {
				return slipperiness * MathHelper.lerp(slideComponent.getTicksSliding() / 60F, 0.45F, 0.8F);
			}
		}
		return slipperiness;
	}

	@ModifyVariable(method = "handleFallDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float enchancement$slideFall(float value) {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
		if (slideComponent != null && slideComponent.shouldSlide()) {
			return Math.max(0, value - 4);
		}
		return value;
	}

	@ModifyVariable(method = "jump", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
	private Vec3d enchancement$slide(Vec3d value) {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
		if (slideComponent != null && slideComponent.shouldSlide()) {
			int ticksSliding = slideComponent.getTicksSliding();
			if (ticksSliding > 0) {
				slideComponent.setShouldSlide(false);
				slideComponent.setTicksSliding(60);
				return value.multiply(MathHelper.lerp(MathHelper.clamp((ticksSliding - 20) / 20F, 0, 1), 8, 1));
			}
		}
		return value;
	}
}
