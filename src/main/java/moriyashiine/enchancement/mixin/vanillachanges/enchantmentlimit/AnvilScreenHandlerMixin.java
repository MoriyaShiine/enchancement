/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
	public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(type, syncId, playerInventory, context);
	}

	@ModifyExpressionValue(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;canCombine(Lnet/minecraft/enchantment/Enchantment;)Z"))
	private boolean enchancement$enchantmentLimit(boolean value) {
		ItemStack stack = input.getStack(0);
		if (EnchancementUtil.limitCheck(false, EnchancementUtil.getNonDefaultEnchantmentsSize(stack, stack.getEnchantments().getSize() + 1) > ModConfig.enchantmentLimit)) {
			return false;
		}
		return value;
	}
}
