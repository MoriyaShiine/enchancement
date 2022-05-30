/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.registry.ModTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ExtractingEvent implements PlayerBlockBreakEvents.Before {
	@Override
	public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		if (!player.isSneaking()) {
			ItemStack stack = player.getMainHandStack();
			if (EnchancementUtil.hasEnchantment(ModEnchantments.EXTRACTING, stack) && state.isIn(ConventionalBlockTags.ORES)) {
				Set<BlockPos> ores = gatherOres(new HashSet<>(), world, new BlockPos.Mutable().set(pos), state.getBlock());
				if (!ores.isEmpty() && ores.size() <= Enchancement.getConfig().maxExtractingBlocks) {
					ItemStack copy = stack.copy();
					AtomicBoolean broken = new AtomicBoolean(false);
					stack.damage(ores.size(), player, stackUser -> {
						stackUser.setStackInHand(Hand.MAIN_HAND, copy);
						broken.set(true);
					});
					if (!broken.get()) {
						BlockState replace = getBaseBlock(state).getDefaultState();
						List<ItemStack> drops = new ArrayList<>();
						ores.forEach(ore -> {
							BlockState oreState = world.getBlockState(ore);
							drops.addAll(Block.getDroppedStacks(oreState, (ServerWorld) world, ore, world.getBlockEntity(ore), player, stack));
							oreState.onStacksDropped((ServerWorld) world, player.getBlockPos(), stack);
							world.breakBlock(ore, false);
							world.setBlockState(ore, replace);
						});
						world.playSound(null, pos, ModSoundEvents.BLOCK_ORE_EXTRACT, SoundCategory.BLOCKS, 1, 1);
						if (!drops.isEmpty()) {
							EnchancementUtil.mergeItemEntities(drops.stream().map(drop -> new ItemEntity(world, player.getX(), player.getY() + 0.5, player.getZ(), drop)).collect(Collectors.toList())).forEach(world::spawnEntity);
						}
						return false;
					}
				}
			}
		}
		return true;
	}

	private static Set<BlockPos> gatherOres(Set<BlockPos> ores, World world, BlockPos.Mutable pos, Block original) {
		if (ores.size() < Enchancement.getConfig().maxExtractingBlocks) {
			int originalX = pos.getX(), originalY = pos.getY(), originalZ = pos.getZ();
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						BlockState state = world.getBlockState(pos.set(originalX + x, originalY + y, originalZ + z));
						if (state.isIn(ConventionalBlockTags.ORES) && !ores.contains(pos) && state.getBlock() == original) {
							ores.add(pos.toImmutable());
							gatherOres(ores, world, pos, original);
						}
					}
				}
			}
		}
		return ores;
	}

	private static Block getBaseBlock(BlockState state) {
		if (state.isIn(ModTags.Blocks.NETHER_ORES)) {
			return Blocks.NETHERRACK;
		} else if (state.isIn(ModTags.Blocks.END_ORES)) {
			return Blocks.END_STONE;
		}
		return Registry.BLOCK.getId(state.getBlock()).getPath().startsWith("deepslate") ? Blocks.DEEPSLATE : Blocks.STONE;
	}
}
