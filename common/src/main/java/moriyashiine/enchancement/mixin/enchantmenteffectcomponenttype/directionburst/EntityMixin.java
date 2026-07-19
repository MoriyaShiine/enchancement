package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.directionburst;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.DirectionBurstComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyReturnValue(method = "getGravity", at = @At("RETURN"))
	private double enchancement$directionBurst(double original) {
		DirectionBurstComponent directionBurst = EnchancementEntityComponents.DIRECTION_BURST.getNullable(this);
		if (directionBurst != null && directionBurst.preventFalling()) {
			return 0;
		}
		return original;
	}
}
