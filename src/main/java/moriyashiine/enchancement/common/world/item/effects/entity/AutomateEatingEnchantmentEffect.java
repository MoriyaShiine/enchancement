/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.tag.ModItemTags;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record AutomateEatingEnchantmentEffect(MinMaxBounds.Ints hungerRange) implements EnchantmentEntityEffect {
	public static final MapCodec<AutomateEatingEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					MinMaxBounds.Ints.CODEC.fieldOf("hunger_range").forGetter(AutomateEatingEnchantmentEffect::hungerRange))
			.apply(instance, AutomateEatingEnchantmentEffect::new));

	@Override
	public MapCodec<AutomateEatingEnchantmentEffect> codec() {
		return CODEC;
	}

	@Override
	public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
		if (entity instanceof Player player && player.canEat(false) && hungerRange().matches(player.getFoodData().getFoodLevel())) {
			ItemStack food = ItemStack.EMPTY;
			if (player.getOffhandItem().has(DataComponents.FOOD)) {
				if (needsFood(player, player.getOffhandItem().get(DataComponents.FOOD)) && isFoodAllowed(player, player.getOffhandItem())) {
					food = player.getOffhandItem();
				}
			} else {
				food = getMostNeededFood(player);
			}
			if (!food.isEmpty()) {
				ItemStack finished = food.finishUsingItem(player.level(), player);
				if (!ItemStack.matches(food, finished) && !player.addItem(finished)) {
					player.spawnAtLocation(serverLevel, finished);
				}
			}
		}
	}

	private static ItemStack getMostNeededFood(Player player) {
		ItemStack food = ItemStack.EMPTY;
		for (int i = 0; i < player.getInventory().getNonEquipmentItems().size(); i++) {
			ItemStack stack = player.getInventory().getNonEquipmentItems().get(i);
			if (stack.has(DataComponents.FOOD)) {
				FoodProperties component = stack.get(DataComponents.FOOD);
				if (needsFood(player, component)) {
					if (food.isEmpty() || food.get(DataComponents.FOOD).nutrition() < component.nutrition()) {
						if (isFoodAllowed(player, stack)) {
							food = stack;
						}
					}
				}
			}
		}
		return food;
	}

	private static boolean needsFood(Player player, FoodProperties component) {
		return component != null && (player.getFoodData().getFoodLevel() < 6 || player.getFoodData().getFoodLevel() <= 20 - component.nutrition());
	}

	private static boolean isFoodAllowed(Player player, ItemStack stack) {
		// todo apoli
//		if (Enchancement.isApoliLoaded) {
//			for (PreventItemUsePower power : PowerHolderComponent.getPowers(player, PreventItemUsePower.class)) {
//				if (power.doesPrevent(stack)) {
//					return false;
//				}
//			}
//		}
		return !stack.is(ModItemTags.CANNOT_AUTOMATICALLY_CONSUME);
	}
}
