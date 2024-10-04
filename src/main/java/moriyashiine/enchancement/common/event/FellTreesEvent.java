/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.world.FellTreesComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModWorldComponents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FellTreesEvent implements PlayerBlockBreakEvents.Before {
	public static final List<Entry> ENTRIES = new ArrayList<>();

	@Override
	public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		ItemStack stack = player.getMainHandStack();
		if (canActivate(player, stack, state)) {
			Entry entry = Entry.get(player);
			if (entry != null && isValid(entry.tree()) && stack.getDamage() + entry.tree().size() <= stack.getMaxDamage()) {
				entry.tree().sort(Comparator.comparingInt(Vec3i::getY).reversed());
				ModWorldComponents.FELL_TREES.get(world).addTree(new FellTreesComponent.Tree(entry.tree(), pos, stack));
				ENTRIES.remove(entry);
				return false;
			}
		}
		return true;
	}

	public static List<BlockPos> gatherTree(List<BlockPos> tree, BlockView world, BlockPos.Mutable pos, Block original) {
		if (tree.size() < ModConfig.maxFellTreesBlocks) {
			int originalX = pos.getX(), originalY = pos.getY(), originalZ = pos.getZ();
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						BlockState state = world.getBlockState(pos.set(originalX + x, originalY + y, originalZ + z));
						if (state.isIn(BlockTags.LOGS) && !tree.contains(pos) && state.getBlock() == original) {
							tree.add(pos.toImmutable());
							gatherTree(tree, world, pos, original);
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

	public static boolean canActivate(PlayerEntity player, ItemStack stack, BlockState state) {
		return !player.isSneaking() && EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.FELL_TREES) && state.isIn(BlockTags.LOGS) && player.canHarvest(state);
	}

	public static boolean isValid(List<BlockPos> tree) {
		return tree.size() > 1 && tree.size() <= ModConfig.maxFellTreesBlocks && isWithinHorizontalBounds(tree);
	}

	public record Entry(PlayerEntity player, List<BlockPos> tree) {
		@Nullable
		public static Entry get(PlayerEntity player) {
			for (Entry entry : ENTRIES) {
				if (entry.player == player) {
					return entry;
				}
			}
			return null;
		}
	}
}
