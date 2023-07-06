/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.delay;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpectralArrowItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ArrowItem.class, SpectralArrowItem.class})
public class DelayArrowItemMixin {
	@Inject(method = "createArrow", at = @At("RETURN"))
	private void enchancement$delay(World world, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> cir) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.DELAY, shooter.getActiveItem())) {
			PersistentProjectileEntity arrow = cir.getReturnValue();
			ModEntityComponents.DELAY.maybeGet(arrow).ifPresent(delayComponent -> {
				delayComponent.setHasDelay(true);
				delayComponent.setStackShotFrom(shooter.getActiveItem());
				delayComponent.sync();
			});
			arrow.setCritical(true);
		}
	}
}
