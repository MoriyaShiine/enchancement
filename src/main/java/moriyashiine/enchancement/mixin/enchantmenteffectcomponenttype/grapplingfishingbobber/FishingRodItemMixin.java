/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.grapplingfishingbobber;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.entity.projectile.StrengthHolder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {
	@WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;spawn(Lnet/minecraft/entity/projectile/ProjectileEntity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/ProjectileEntity;"))
	private <T extends ProjectileEntity> T enchancement$grappleFishingBobber(T projectile, ServerWorld world, ItemStack stack, Operation<T> original, World world0, PlayerEntity user) {
		if (projectile instanceof StrengthHolder strengthHolder) {
			float grapplingStrength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.GRAPPLING_FISHING_BOBBER, world, stack, 0);
			if (grapplingStrength != 0) {
				strengthHolder.enchancement$setStrength(grapplingStrength);
			}
		}
		return original.call(projectile, world, stack);
	}
}
