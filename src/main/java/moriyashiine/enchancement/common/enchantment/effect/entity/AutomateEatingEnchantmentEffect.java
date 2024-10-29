/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.tag.ModItemTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record AutomateEatingEnchantmentEffect(NumberRange.IntRange hungerRange) implements EnchantmentEntityEffect {
	public static final MapCodec<AutomateEatingEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					NumberRange.IntRange.CODEC.fieldOf("hunger_range").forGetter(AutomateEatingEnchantmentEffect::hungerRange))
			.apply(instance, AutomateEatingEnchantmentEffect::new));

	@Override
	public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
		if (user instanceof PlayerEntity player && player.canConsume(false) && hungerRange().test(player.getHungerManager().getFoodLevel())) {
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
					player.dropStack(world, finished);
				}
			}
		}
	}

	@Override
	public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
		return CODEC;
	}

	private static ItemStack getMostNeededFood(PlayerEntity player) {
		ItemStack food = ItemStack.EMPTY;
		for (int i = 0; i < player.getInventory().main.size(); i++) {
			ItemStack stack = player.getInventory().main.get(i);
			if (stack.contains(DataComponentTypes.FOOD) && !stack.isIn(ModItemTags.CANNOT_AUTOMATICALLY_CONSUME)) {
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
