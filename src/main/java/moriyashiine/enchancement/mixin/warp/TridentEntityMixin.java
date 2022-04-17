package moriyashiine.enchancement.mixin.warp;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public class TridentEntityMixin {
	@Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$warp(World world, LivingEntity owner, ItemStack stack, CallbackInfo ci) {
		if (EnchantmentHelper.getLevel(ModEnchantments.WARP, stack) > 0 || (owner instanceof DrownedEntity && EnchantmentHelper.getEquipmentLevel(ModEnchantments.WARP, owner) > 0)) {
			ModEntityComponents.WARP.maybeGet(this).ifPresent(warpComponent -> {
				warpComponent.setHasWarp(true);
				warpComponent.sync();
			});
		}
	}
}
