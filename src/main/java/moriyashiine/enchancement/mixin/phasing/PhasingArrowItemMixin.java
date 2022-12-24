/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.phasing;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
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
public class PhasingArrowItemMixin {
	@Inject(method = "createArrow", at = @At("RETURN"))
	private void enchancement$phasing(World world, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> cir) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.PHASING, shooter)) {
			PersistentProjectileEntity arrow = cir.getReturnValue();
			ModEntityComponents.PHASHING.maybeGet(arrow).ifPresent(phasingComponent -> {
				phasingComponent.setShouldPhase(true);
				phasingComponent.sync();
			});
			arrow.setNoGravity(true);
		}
	}
}
