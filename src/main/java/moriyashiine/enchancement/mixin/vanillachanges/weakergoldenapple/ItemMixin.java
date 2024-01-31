/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.weakergoldenapple;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Item.class)
public class ItemMixin {
	@Unique
	private static ItemStack cachedStack = null;

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;canConsume(Z)Z", shift = At.Shift.BY, by = -2), locals = LocalCapture.CAPTURE_FAILHARD)
	private void enchancement$weakerGoldenApple(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, ItemStack itemStack) {
		cachedStack = itemStack;
	}

	@ModifyArg(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;canConsume(Z)Z"))
	private boolean enchancement$weakerGoldenApple(boolean value) {
		if (ModConfig.weakerGoldenApple) {
			if (cachedStack.isOf(Items.GOLDEN_APPLE) || cachedStack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
				return false;
			}
		}
		return value;
	}
}
