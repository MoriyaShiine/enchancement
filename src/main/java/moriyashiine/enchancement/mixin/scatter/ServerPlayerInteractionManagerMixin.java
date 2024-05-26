/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.scatter;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
	@ModifyExpressionValue(method = "interactItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ItemCooldownManager;isCoolingDown(Lnet/minecraft/item/Item;)Z"))
	private boolean enchancement$scatter(boolean value, ServerPlayerEntity player, World world, ItemStack stack) {
		if (player.getItemCooldownManager().isCoolingDown(stack.getItem()) && stack.isOf(Items.CROSSBOW) && EnchancementUtil.hasEnchantment(ModEnchantments.SCATTER, stack) && !CrossbowItem.isCharged(stack)) {
			return false;
		}
		return value;
	}
}
