/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.rebalancegoldenapples;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public class ItemMixin {
	@WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;canConsume(Z)Z"))
	private boolean enchancement$rebalanceGoldenApples(PlayerEntity instance, boolean ignoreHunger, Operation<Boolean> original, World world, PlayerEntity user, Hand hand) {
		if (ModConfig.rebalanceGoldenApples) {
			ItemStack stack = user.getStackInHand(hand);
			if (stack.isOf(Items.GOLDEN_APPLE) || stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
				ignoreHunger = false;
			}
		}
		return original.call(instance, ignoreHunger);
	}
}
