/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.chaos;

import moriyashiine.enchancement.common.component.entity.ChaosArrowComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowEntity.class)
public class ArrowEntityMixin {
	@Inject(method = "asItemStack", at = @At("HEAD"), cancellable = true)
	private void enchancement$chaos(CallbackInfoReturnable<ItemStack> cir) {
		ChaosArrowComponent chaosArrowComponent = ModEntityComponents.CHAOS_ARROW.get(this);
		if (!chaosArrowComponent.getOriginalStack().isEmpty()) {
			cir.setReturnValue(chaosArrowComponent.getOriginalStack());
		}
	}
}
