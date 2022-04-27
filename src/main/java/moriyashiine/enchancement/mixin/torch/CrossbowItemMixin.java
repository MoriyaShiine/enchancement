package moriyashiine.enchancement.mixin.torch;

import moriyashiine.enchancement.common.entity.projectile.TorchEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	@ModifyVariable(method = "createArrow", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
	private static PersistentProjectileEntity enchancemenet$torch(PersistentProjectileEntity value, World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow) {
		if (arrow.isOf(Items.TORCH)) {
			return new TorchEntity(world, entity);
		}
		return value;
	}
}
