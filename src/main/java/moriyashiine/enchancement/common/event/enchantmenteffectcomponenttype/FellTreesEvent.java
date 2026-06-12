/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.component.level.FellTreesComponent;
import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.EnchancementLevelComponents;
import moriyashiine.enchancement.common.tag.EnchancementBlockTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.ModifyDestroySpeedEvent;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FellTreesEvent {
	public static void init() {
		ModifyDestroySpeedEvent.MULTIPLY_TOTAL.register(new DestroySpeed());
		PlayerBlockBreakEvents.BEFORE.register(new FellTree());
	}

	public static final List<Entry> ENTRIES = new ArrayList<>();

	private static List<BlockPos> gatherTree(List<BlockPos> tree, BlockGetter level, BlockPos.MutableBlockPos pos, Block original) {
		if (tree.size() < EnchancementConfig.maxFellTreesBlocks) {
			int originalX = pos.getX(), originalY = pos.getY(), originalZ = pos.getZ();
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						pos.set(originalX + x, originalY + y, originalZ + z);
						if (!(level instanceof Level borderLevel) || borderLevel.getWorldBorder().isWithinBounds(pos)) {
							BlockState state = level.getBlockState(pos);
							if (state.is(EnchancementBlockTags.FELLABLE) && !tree.contains(pos) && state.getBlock() == original) {
								tree.add(pos.immutable());
								gatherTree(tree, level, pos, original);
							}
						}
					}
				}
			}
		}
		return tree;
	}

	private static boolean isWithinHorizontalBounds(List<BlockPos> tree) {
		Integer minX = null, maxX = null, minZ = null, maxZ = null;
		for (BlockPos pos : tree) {
			if (minX == null || pos.getX() < minX) {
				minX = pos.getX();
			}
			if (maxX == null || pos.getX() > maxX) {
				maxX = pos.getX();
			}
			if (minZ == null || pos.getZ() < minZ) {
				minZ = pos.getZ();
			}
			if (maxZ == null || pos.getZ() > maxZ) {
				maxZ = pos.getZ();
			}
		}
		if (minX == null) {
			return false;
		}
		return Math.abs(maxX - minX) < EnchancementConfig.maxFellTreesHorizontalLength && Math.abs(maxZ - minZ) < EnchancementConfig.maxFellTreesHorizontalLength;
	}

	private static boolean canActivate(Player player, ItemStack stack, BlockState state) {
		return !player.isShiftKeyDown() && EnchantmentHelper.has(stack, EnchancementEnchantmentEffectComponentTypes.FELL_TREES) && state.is(EnchancementBlockTags.FELLABLE) && player.hasCorrectToolForDrops(state);
	}

	private static boolean isValid(List<BlockPos> tree, ItemStack stack) {
		if (!stack.isDamageableItem() || stack.getDamageValue() + tree.size() <= stack.getMaxDamage()) {
			return tree.size() > 1 && tree.size() <= EnchancementConfig.maxFellTreesBlocks && isWithinHorizontalBounds(tree);
		}
		return false;
	}

	private static class DestroySpeed implements ModifyDestroySpeedEvent {
		@Override
		public float modify(Player player, ItemStack stack, Level level, BlockState state, @Nullable BlockPos pos) {
			if (pos != null && canActivate(player, stack, state)) {
				Entry entry = Entry.get(player);
				if (entry == null) {
					entry = new Entry(player, gatherTree(new ArrayList<>(), level, new BlockPos.MutableBlockPos().set(pos), state.getBlock()));
					ENTRIES.add(entry);
				}
				if (isValid(entry.tree(), stack)) {
					float fellTreesSpeed = EnchancementUtil.getValue(EnchancementEnchantmentEffectComponentTypes.FELL_TREES, player.getRandom(), stack, 0);
					return Mth.lerp(Math.min(1, entry.tree().size() / 32F), fellTreesSpeed, fellTreesSpeed * 0.05F);
				}
			}
			return 1;
		}
	}

	private static class FellTree implements PlayerBlockBreakEvents.Before {
		@Override
		public boolean beforeBlockBreak(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
			ItemStack stack = player.getMainHandItem();
			if (canActivate(player, stack, state)) {
				Entry entry = Entry.get(player);
				if (entry != null && isValid(entry.tree(), stack)) {
					entry.tree().sort(Comparator.comparingInt(Vec3i::getY).reversed());
					EnchancementLevelComponents.FELL_TREES.get(level).addTree(FellTreesComponent.Tree.of(entry.tree(), pos, stack));
					ENTRIES.remove(entry);
					stack.hurtAndBreak(entry.tree().size(), player, EquipmentSlot.MAINHAND);
					return false;
				}
			}
			return true;
		}
	}

	public record Entry(Player player, List<BlockPos> tree) {
		public static @Nullable Entry get(Player player) {
			for (Entry entry : ENTRIES) {
				if (entry.player == player) {
					return entry;
				}
			}
			return null;
		}
	}
}
