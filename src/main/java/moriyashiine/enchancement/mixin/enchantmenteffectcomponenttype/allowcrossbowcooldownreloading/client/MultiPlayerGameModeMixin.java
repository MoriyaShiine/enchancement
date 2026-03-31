/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.allowcrossbowcooldownreloading.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
	@ModifyExpressionValue(method = "lambda$useItem$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemCooldowns;isOnCooldown(Lnet/minecraft/world/item/ItemStack;)Z"))
	private boolean enchancement$allowCrossbowCooldownReloading(boolean value, @Local(argsOnly = true) Player player, @Local(name = "itemStack") ItemStack itemStack) {
		if (player.getCooldowns().isOnCooldown(itemStack) && itemStack.is(Items.CROSSBOW) && EnchantmentHelper.has(itemStack, ModEnchantmentEffectComponentTypes.ALLOW_CROSSBOW_COOLDOWN_RELOADING) && !CrossbowItem.isCharged(itemStack)) {
			return false;
		}
		return value;
	}
}
