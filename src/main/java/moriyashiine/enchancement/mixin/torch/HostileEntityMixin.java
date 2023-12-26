/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.torch;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(HostileEntity.class)
public class HostileEntityMixin {
	@ModifyVariable(method = "getProjectileType", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/RangedWeaponItem;getHeldProjectiles()Ljava/util/function/Predicate;"))
	private Predicate<ItemStack> enchancement$torch(Predicate<ItemStack> value, ItemStack stack) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, stack)) {
			value = projectile -> projectile.isOf(Items.TORCH);
		}
		return value;
	}
}
