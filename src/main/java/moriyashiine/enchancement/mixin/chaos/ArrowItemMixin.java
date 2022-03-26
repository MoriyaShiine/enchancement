package moriyashiine.enchancement.mixin.chaos;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowItem.class)
public class ArrowItemMixin {
	@Inject(method = "createArrow", at = @At("HEAD"), cancellable = true)
	private void enchancement$chaosModifyArrow(World world, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> cir) {
		if (stack.getItem() == Items.ARROW && EnchantmentHelper.getEquipmentLevel(ModEnchantments.CHAOS, shooter) > 0) {
			ArrowEntity arrow = new ArrowEntity(world, shooter);
			arrow.initFromStack(PotionUtil.setPotion(new ItemStack(Items.TIPPED_ARROW), Registry.POTION.get(shooter.getRandom().nextInt(Registry.POTION.size()))));
			arrow.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
			cir.setReturnValue(arrow);
		}
	}
}
