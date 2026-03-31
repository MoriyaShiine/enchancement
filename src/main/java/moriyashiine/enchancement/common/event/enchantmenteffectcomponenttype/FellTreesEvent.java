/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.level.FellTreesComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModLevelComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.ModifyDestroyProgressEvent;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
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
	public static final List<Entry> ENTRIES = new ArrayList<>();

	public static List<BlockPos> gatherTree(List<BlockPos> tree, BlockGetter level, BlockPos.MutableBlockPos pos, Block original) {
		if (tree.size() < ModConfig.maxFellTreesBlocks) {
			int originalX = pos.getX(), originalY = pos.getY(), originalZ = pos.getZ();
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						pos.set(originalX + x, originalY + y, originalZ + z);
						if (!(level instanceof Level borderLevel) || borderLevel.getWorldBorder().isWithinBounds(pos)) {
							BlockState state = level.getBlockState(pos);
							if (state.is(BlockTags.LOGS) && !tree.contains(pos) && state.getBlock() == original) {
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

	public static boolean isWithinHorizontalBounds(List<BlockPos> tree) {
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
		return Math.abs(maxX - minX) < ModConfig.maxFellTreesHorizontalLength && Math.abs(maxZ - minZ) < ModConfig.maxFellTreesHorizontalLength;
	}

	public static boolean canActivate(Player player, ItemStack stack, BlockState state) {
		return !player.isShiftKeyDown() && EnchantmentHelper.has(stack, ModEnchantmentEffectComponentTypes.FELL_TREES) && state.is(BlockTags.LOGS) && player.hasCorrectToolForDrops(state);
	}

	public static boolean isValid(List<BlockPos> tree, ItemStack stack) {
		if (!stack.isDamageableItem() || stack.getDamageValue() + tree.size() <= stack.getMaxDamage()) {
			return tree.size() > 1 && tree.size() <= ModConfig.maxFellTreesBlocks && isWithinHorizontalBounds(tree);
		}
		return false;
	}

	public static class BreakSpeed implements ModifyDestroyProgressEvent {
		@Override
		public float modify(Player player, BlockState state, BlockGetter level, BlockPos pos) {
			if (canActivate(player, player.getMainHandItem(), state)) {
				Entry entry = Entry.get(player);
				if (entry == null) {
					entry = new Entry(player, gatherTree(new ArrayList<>(), level, new BlockPos.MutableBlockPos().set(pos), state.getBlock()));
					ENTRIES.add(entry);
				}
				if (isValid(entry.tree(), player.getMainHandItem())) {
					float fellTreesSpeed = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.FELL_TREES, player.getRandom(), player.getMainHandItem(), 0);
					return Mth.lerp(Math.min(1, entry.tree().size() / 32F), fellTreesSpeed, fellTreesSpeed * 0.05F);
				}
			}
			return 1;
		}
	}

	public static class FellTree implements PlayerBlockBreakEvents.Before {
		@Override
		public boolean beforeBlockBreak(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
			ItemStack stack = player.getMainHandItem();
			if (canActivate(player, stack, state)) {
				Entry entry = Entry.get(player);
				if (entry != null && isValid(entry.tree(), stack)) {
					entry.tree().sort(Comparator.comparingInt(Vec3i::getY).reversed());
					ModLevelComponents.FELL_TREES.get(level).addTree(FellTreesComponent.Tree.of(entry.tree(), pos, stack));
					ENTRIES.remove(entry);
					stack.hurtAndBreak(entry.tree().size(), player, EquipmentSlot.MAINHAND);
					return false;
				}
			}
			return true;
		}
	}

	public record Entry(Player player, List<BlockPos> tree) {
		@Nullable
		public static Entry get(Player player) {
			for (Entry entry : ENTRIES) {
				if (entry.player == player) {
					return entry;
				}
			}
			return null;
		}
	}
}
