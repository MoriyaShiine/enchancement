/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.chaos;

import moriyashiine.enchancement.common.component.entity.ChaosArrowComponent;
import moriyashiine.enchancement.common.component.entity.ChaosSpectralArrowComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpectralArrowItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpectralArrowItem.class)
public class SpectralArrowItemMixin {
	@Inject(method = "createArrow", at = @At("RETURN"))
	private void enchancement$chaos(World world, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> cir) {
		if (cir.getReturnValue() instanceof SpectralArrowEntity spectralArrow) {
			ChaosArrowComponent.applyChaos(shooter, stack, statusEffects -> {
				ChaosSpectralArrowComponent chaosSpectralArrowComponent = ModEntityComponents.CHAOS_SPECTRAL_ARROW.get(spectralArrow);
				chaosSpectralArrowComponent.setEffects(statusEffects);
				chaosSpectralArrowComponent.sync();
			});
		}
	}
}
