/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {
	@WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectile(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/projectile/Projectile;"))
	private <T extends Projectile> T enchancement$rebalanceEquipment(T projectile, ServerLevel serverLevel, ItemStack itemStack, Operation<T> original, Level level, Player player) {
		if (ModConfig.rebalanceEquipment) {
			float pow = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.GRAPPLING_FISHING_BOBBER, serverLevel, itemStack, 0) != 0 ? 2.75F : 1.25F;
			projectile.snapTo(player.getX(), player.getEyeY(), player.getZ(), player.getYRot(), player.getXRot());
			projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, pow, 0);
		}
		return original.call(projectile, serverLevel, itemStack);
	}
}
