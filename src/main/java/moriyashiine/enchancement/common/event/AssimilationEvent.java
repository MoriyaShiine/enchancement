/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class AssimilationEvent implements ServerTickEvents.EndTick {
	@Override
	public void onEndTick(MinecraftServer server) {
		server.getPlayerManager().getPlayerList().forEach(player -> {
			if (player.age % 20 == 0 && player.getHungerManager().getFoodLevel() <= 14 && EnchancementUtil.hasEnchantment(ModEnchantments.ASSIMILATION, player)) {
				ItemStack food = ItemStack.EMPTY;
				if (player.getOffHandStack().contains(DataComponentTypes.FOOD)) {
					if (needsFood(player, player.getOffHandStack().get(DataComponentTypes.FOOD)) && isFoodAllowed(player, player.getOffHandStack())) {
						food = player.getOffHandStack();
					}
				} else {
					food = getMostNeededFood(player);
				}
				if (!food.isEmpty()) {
					ItemStack finished = food.finishUsing(player.getWorld(), player);
					if (!ItemStack.areEqual(food, finished) && !player.giveItemStack(finished)) {
						player.dropStack(finished);
					}
				}
			}
		});
	}

	private static ItemStack getMostNeededFood(PlayerEntity player) {
		ItemStack food = ItemStack.EMPTY;
		for (int i = 0; i < player.getInventory().main.size(); i++) {
			ItemStack stack = player.getInventory().main.get(i);
			if (stack.contains(DataComponentTypes.FOOD) && !stack.isIn(ModTags.Items.CANNOT_ASSIMILATE)) {
				FoodComponent component = stack.get(DataComponentTypes.FOOD);
				if (needsFood(player, component)) {
					if (food.isEmpty() || food.get(DataComponentTypes.FOOD).nutrition() < component.nutrition()) {
						if (isFoodAllowed(player, stack)) {
							food = stack;
						}
					}
				}
			}
		}
		return food;
	}

	private static boolean needsFood(PlayerEntity player, FoodComponent component) {
		return component != null && (player.getHungerManager().getFoodLevel() < 6 || player.getHungerManager().getFoodLevel() <= 20 - component.nutrition());
	}

	private static boolean isFoodAllowed(PlayerEntity player, ItemStack stack) {
		// todo apoli
//		if (Enchancement.isApoliLoaded) {
//			for (PreventItemUsePower power : PowerHolderComponent.getPowers(player, PreventItemUsePower.class)) {
//				if (power.doesPrevent(stack)) {
//					return false;
//				}
//			}
//		}
		return true;
	}
}
