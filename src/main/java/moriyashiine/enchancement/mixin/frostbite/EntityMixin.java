package moriyashiine.enchancement.mixin.frostbite;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "setFrozenTicks", at = @At("HEAD"))
	private void enchancement$frostbite(int frozenTicks, CallbackInfo ci) {
		if (frozenTicks <= 0) {
			ModEntityComponents.FROZEN.maybeGet(this).ifPresent(frozenComponent -> {
				if (!frozenComponent.isFrozen()) {
					frozenComponent.setLastFreezingAttacker(null);
				}
			});
		}
	}
}
