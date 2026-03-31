/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rapidcrossbowfire.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow
	@Nullable
	public LocalPlayer player;

	@ModifyExpressionValue(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z", ordinal = 2))
	private boolean enchancement$rapidCrossbowFire(boolean value) {
		if (player != null) {
			ItemStack stack = player.getUseItem();
			if (stack.getItem() instanceof CrossbowItem && EnchantmentHelper.has(stack, ModEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE)) {
				float powerForTime = CrossbowItem.getPowerForTime(stack.getUseDuration(player) - player.getUseItemRemainingTicks(), stack, player);
				if (powerForTime >= 1) {
					return false;
				}
			}
		}
		return value;
	}
}
