package moriyashiine.enchancement.mixin.bury;

import moriyashiine.enchancement.common.component.entity.BuryComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
	private Vec3d enchancement$bury(Vec3d value, MovementType movementType) {
		if (movementType == MovementType.SELF) {
			BuryComponent buryComponent = ModEntityComponents.BURY.getNullable(this);
			if (buryComponent != null && buryComponent.getBuryPos() != null) {
				return Vec3d.ZERO;
			}
		}
		return value;
	}
}
