/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.scatter;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@ModifyReturnValue(method = "getProjectileType", at = @At(value = "RETURN", ordinal = 3))
	private ItemStack enchancement$scatter(ItemStack original, ItemStack weaponStack) {
		if (!original.isEmpty() && EnchancementUtil.hasEnchantment(ModEnchantments.SCATTER, weaponStack)) {
			return Items.AMETHYST_SHARD.getDefaultStack();
		}
		return original;
	}
}
