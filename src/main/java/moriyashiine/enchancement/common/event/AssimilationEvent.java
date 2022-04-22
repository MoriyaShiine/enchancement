package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class AssimilationEvent implements ServerTickEvents.EndTick {
	@Override
	public void onEndTick(MinecraftServer server) {
		server.getPlayerManager().getPlayerList().forEach(player -> {
			if (player.age % 20 == 0 && player.getHungerManager().isNotFull() && EnchancementUtil.hasEnchantment(ModEnchantments.ASSIMILATION, player)) {
				ItemStack food = ItemStack.EMPTY;
				if (player.getOffHandStack().isFood()) {
					if (needsFood(player, player.getOffHandStack().getItem().getFoodComponent())) {
						food = player.getOffHandStack();
					}
				} else {
					food = getMostNeededFood(player);
				}
				if (!food.isEmpty()) {
					player.eatFood(player.world, food);
				}
			}
		});
	}

	@SuppressWarnings("ConstantConditions")
	private static ItemStack getMostNeededFood(PlayerEntity player) {
		ItemStack food = ItemStack.EMPTY;
		for (int i = 0; i < player.getInventory().main.size(); i++) {
			ItemStack stack = player.getInventory().main.get(i);
			if (stack.isFood()) {
				FoodComponent component = stack.getItem().getFoodComponent();
				if (needsFood(player, component)) {
					if (food.isEmpty() || food.getItem().getFoodComponent().getHunger() < component.getHunger()) {
						food = stack;
					}
				}
			}
		}
		return food;
	}

	private static boolean needsFood(PlayerEntity player, FoodComponent component) {
		return component != null && (player.getHungerManager().getFoodLevel() < 6 || player.getHungerManager().getFoodLevel() <= 20 - component.getHunger());
	}
}
