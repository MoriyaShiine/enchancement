/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.chaos;

import moriyashiine.enchancement.common.component.entity.ChaosArrowComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.mixin.util.accessor.ArrowEntityAccessor;
import moriyashiine.enchancement.mixin.util.accessor.PersistentProjectileEntityAccessor;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ArrowItem.class)
public class ArrowItemMixin {
	@Inject(method = "createArrow", at = @At("RETURN"))
	private void enchancement$chaos(World world, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> cir) {
		if (cir.getReturnValue() instanceof ArrowEntity arrow) {
			ChaosArrowComponent.applyChaos(shooter, stack, statusEffects -> {
				ModEntityComponents.CHAOS_ARROW.get(arrow).setOriginalStack(((PersistentProjectileEntityAccessor) arrow).enchancement$asItemStack());
				((ArrowEntityAccessor) arrow).enchancement$setPotionContents(new PotionContentsComponent(Optional.empty(), Optional.empty(), statusEffects));
			});
		}
	}
}
