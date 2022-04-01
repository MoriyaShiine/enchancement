package moriyashiine.enchancement.mixin.dash;

import moriyashiine.enchancement.common.component.entity.DashComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "jump", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
	private Vec3d enchancement$dash(Vec3d value) {
		DashComponent dashComponent = ModEntityComponents.DASH.getNullable(this);
		if (dashComponent != null && dashComponent.shouldWavedash()) {
			dashComponent.setDashCooldown(0);
			return value.multiply(2.5);
		}
		return value;
	}
}
