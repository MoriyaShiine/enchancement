/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.grapplingfishingbobber;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.StrengthHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {
	@SuppressWarnings("WrapWithConditionTargetsNonVoid")
	@WrapWithCondition(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectile(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/projectile/Projectile;"))
	private <T extends Projectile> boolean enchancement$grappleFishingBobber(T projectile, ServerLevel serverLevel, ItemStack itemStack) {
		if (projectile instanceof StrengthHolder strengthHolder) {
			float grapplingStrength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.GRAPPLING_FISHING_BOBBER, serverLevel, itemStack, 0);
			if (grapplingStrength != 0) {
				strengthHolder.enchancement$setStrength(grapplingStrength);
			}
		}
		return true;
	}
}
