/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.level.WideMiningComponent;
import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.EnchancementLevelComponents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class WideMiningEvent implements PlayerBlockBreakEvents.After {
	public static void init() {
		PlayerBlockBreakEvents.AFTER.register(new WideMiningEvent());
	}

	public static boolean canActivate(Player player, ItemStack stack, BlockState state) {
		return !player.isShiftKeyDown() && EnchantmentHelper.has(stack, EnchancementEnchantmentEffectComponentTypes.WIDE_MINING) && stack.isCorrectToolForDrops(state);
	}

	public static boolean isValid(List<BlockPos> blocks, ItemStack stack) {
		if (!stack.isDamageableItem() || stack.getDamageValue() + blocks.size() <= stack.getMaxDamage()) {
			return blocks.size() > 1;
		}
		return false;
	}

	@Override
	public void afterBlockBreak(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
		ItemStack stack = player.getMainHandItem();
		if (canActivate(player, stack, state)) {
			WideMiningComponent wideMining = EnchancementLevelComponents.WIDE_MINING.get(level);
			WideMiningComponent.Entry entry = wideMining.getEntry(player);
			if (entry != null && isValid(entry.blocks(), stack)) {
				int numBroken = 0;
				for (BlockPos blockPos : entry.blocks()) {
					if (player.mayInteract((ServerLevel) level, blockPos)) {
						level.destroyBlock(blockPos, !player.getAbilities().instabuild, player);
						numBroken++;
					}
				}
				stack.hurtAndBreak(numBroken, player, EquipmentSlot.MAINHAND);
				wideMining.removeEntry(entry.player());
				wideMining.sync();
			}
		}
	}
}
