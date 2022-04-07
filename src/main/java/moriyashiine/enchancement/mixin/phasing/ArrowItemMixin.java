package moriyashiine.enchancement.mixin.phasing;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowItem.class)
public class ArrowItemMixin {
	@Inject(method = "createArrow", at = @At("RETURN"))
	private void enchancement$phasing(World world, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> cir) {
		if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.PHASING, shooter) > 0) {
			PersistentProjectileEntity arrow = cir.getReturnValue();
			ModEntityComponents.PHASHING.get(arrow).setShouldPhase(true);
			arrow.setNoGravity(true);
		}
	}
}
