/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.assimilation.LivingEntityAccessor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.UseAction;

public class AssimilationEvent implements ServerTickEvents.EndTick {
	@Override
	public void onEndTick(MinecraftServer server) {
		server.getPlayerManager().getPlayerList().forEach(player -> {
			if (player.age % 20 == 0 && player.getHungerManager().getFoodLevel() <= 14 && EnchancementUtil.hasEnchantment(ModEnchantments.ASSIMILATION, player)) {
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
			if (player.getActiveItem().isFood() || player.getActiveItem().getUseAction() == UseAction.DRINK) {
				if (player.getItemUseTime() == player.getActiveItem().getMaxUseTime() * 0.75F && EnchancementUtil.hasEnchantment(ModEnchantments.ASSIMILATION, player)) {
					((LivingEntityAccessor) player).enchancement$consumeItem();
				}
			}
		});
	}

	@SuppressWarnings("ConstantConditions")
	private static ItemStack getMostNeededFood(PlayerEntity player) {
		ItemStack food = ItemStack.EMPTY;
		for (int i = 0; i < player.getInventory().main.size(); i++) {
			ItemStack stack = player.getInventory().main.get(i);
			if (stack.isFood() && !stack.isIn(ModTags.Items.CANNOT_ASSIMILATE)) {
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
