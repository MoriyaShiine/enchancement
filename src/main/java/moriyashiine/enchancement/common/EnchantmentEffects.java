package moriyashiine.enchancement.common;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;

public class EnchantmentEffects {
	public static boolean isGroundedOrJumping(LivingEntity living) {
		if (living instanceof PlayerEntity player && player.getAbilities().flying) {
			return false;
		}
		return !living.isTouchingWater() && !living.isSwimming();
	}

	public static void tickAssimilation(PlayerEntity player) {
		if (player.age % 20 == 0 && player.getHungerManager().isNotFull() && EnchantmentHelper.getLevel(ModEnchantments.ASSIMILATION, player.getEquippedStack(EquipmentSlot.HEAD)) > 0) {
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
	}

	public static void tickBuffet(PlayerEntity player, Runnable runnable) {
		if (player.getActiveItem().isFood() || player.getActiveItem().getUseAction() == UseAction.DRINK) {
			if (player.getItemUseTime() == player.getActiveItem().getMaxUseTime() / 2 && EnchantmentHelper.getLevel(ModEnchantments.BUFFET, player.getEquippedStack(EquipmentSlot.HEAD)) > 0) {
				runnable.run();
			}
		}
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
