/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.world.LumberjackComponent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModWorldComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LumberjackEvent implements PlayerBlockBreakEvents.Before {
	@Override
	public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		if (!player.isSneaking()) {
			ItemStack stack = player.getMainHandStack();
			if (EnchancementUtil.hasEnchantment(ModEnchantments.LUMBERJACK, stack) && state.isIn(BlockTags.LOGS) && player.canHarvest(state)) {
				List<BlockPos> tree = gatherTree(new ArrayList<>(), world, new BlockPos.Mutable().set(pos), state.getBlock());
				if (tree.size() > 1 && tree.size() <= ModConfig.maxLumberjackBlocks && isWithinHorizontalBounds(tree)) {
					boolean[] broken = {false};
					stack.copy().damage(tree.size(), world.getRandom(), null, () -> broken[0] = true);
					if (!broken[0]) {
						tree.sort(Comparator.comparingInt(Vec3i::getY).reversed());
						ModWorldComponents.LUMBERJACK.get(world).addTree(new LumberjackComponent.Tree(tree, pos));
						return false;
					}
				}
			}
		}
		return true;
	}

	private static List<BlockPos> gatherTree(List<BlockPos> tree, World world, BlockPos.Mutable pos, Block original) {
		if (tree.size() < ModConfig.maxLumberjackBlocks) {
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
		return Math.abs(maxX - minX) < ModConfig.maxLumberjackHorizontalLength && Math.abs(maxZ - minZ) < ModConfig.maxLumberjackHorizontalLength;
	}
}
