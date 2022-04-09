package moriyashiine.enchancement.mixin.berserk;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(method = "canFoodHeal", at = @At("HEAD"), cancellable = true)
	private void enchancment$berserk(CallbackInfoReturnable<Boolean> cir) {
		if (ModEntityComponents.BERSERK.get(this).getPreventRegenerationTicks() > 0) {
			cir.setReturnValue(false);
		}
	}
}
