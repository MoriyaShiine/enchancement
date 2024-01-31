/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.torch.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.torch.CrossbowItemAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.CrossbowItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@ModifyExpressionValue(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z", ordinal = 2))
	private boolean enchancement$torch(boolean value) {
		if (player.getActiveItem().getItem() instanceof CrossbowItem && EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, player.getActiveItem())) {
			float progress = CrossbowItemAccessor.enchancement$getPullProgress(player.getActiveItem().getMaxUseTime() - player.getItemUseTimeLeft(), player.getActiveItem());
			if (progress >= 1) {
				return false;
			}
		}
		return value;
	}
}
