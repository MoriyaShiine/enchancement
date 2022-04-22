package moriyashiine.enchancement.mixin.warp;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
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
		if (EnchancementUtil.hasEnchantment(ModEnchantments.WARP, stack) || (owner instanceof DrownedEntity && EnchancementUtil.hasEnchantment(ModEnchantments.WARP, owner))) {
			ModEntityComponents.WARP.maybeGet(this).ifPresent(warpComponent -> {
				warpComponent.setHasWarp(true);
				warpComponent.sync();
			});
		}
	}
}
