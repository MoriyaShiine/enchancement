/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.allowcrossbowcooldownreloading;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
	@ModifyExpressionValue(method = "useItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemCooldowns;isOnCooldown(Lnet/minecraft/world/item/ItemStack;)Z"))
	private boolean enchancement$allowCrossbowCooldownReloading(boolean value, ServerPlayer player, Level level, ItemStack itemStack) {
		if (player.getCooldowns().isOnCooldown(itemStack) && itemStack.is(Items.CROSSBOW) && EnchantmentHelper.has(itemStack, ModEnchantmentEffectComponentTypes.ALLOW_CROSSBOW_COOLDOWN_RELOADING) && !CrossbowItem.isCharged(itemStack)) {
			return false;
		}
		return value;
	}
}
