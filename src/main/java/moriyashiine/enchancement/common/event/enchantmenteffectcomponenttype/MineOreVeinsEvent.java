/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.tag.ModBlockTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.ModifyBlockBreakingSpeedEvent;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class MineOreVeinsEvent {
	public static final Map<Block, Block> BASE_BLOCK_MAP = new HashMap<>();

	public static Set<BlockPos> gatherOres(Set<BlockPos> ores, World world, BlockPos.Mutable pos, Block original) {
		if (ores.size() < ModConfig.maxMineOreVeinsBlocks) {
			int originalX = pos.getX(), originalY = pos.getY(), originalZ = pos.getZ();
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						if (world.getWorldBorder().contains(pos.set(originalX + x, originalY + y, originalZ + z))) {
							BlockState state = world.getBlockState(pos);
							if (state.isIn(ConventionalBlockTags.ORES) && !ores.contains(pos) && state.getBlock() == original) {
								ores.add(pos.toImmutable());
								gatherOres(ores, world, pos, original);
							}
						}
					}
				}
			}
		}
		return ores;
	}

	public static boolean canActivate(PlayerEntity player, ItemStack stack, BlockState state) {
		return !player.isSneaking() && EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.MINE_ORE_VEINS) && state.isIn(ConventionalBlockTags.ORES) && player.canHarvest(state);
	}

	public static boolean isValid(Set<BlockPos> ores, ItemStack stack) {
		if (!stack.isDamageable() || stack.getDamage() + ores.size() <= stack.getMaxDamage()) {
			return !ores.isEmpty() && ores.size() <= ModConfig.maxMineOreVeinsBlocks;
		}
		return false;
	}

	private static Block getBaseBlock(BlockState state) {
		Block baseBlock = BASE_BLOCK_MAP.get(state.getBlock());
		if (baseBlock != null) {
			return baseBlock;
		}
		if (state.isIn(ModBlockTags.NETHER_ORES)) {
			return Blocks.NETHERRACK;
		} else if (state.isIn(ModBlockTags.END_ORES)) {
			return Blocks.END_STONE;
		} else if (state.isIn(ConventionalBlockTags.ORES_IN_GROUND_DEEPSLATE)) {
			return Blocks.DEEPSLATE;
		}
		return Blocks.STONE;
	}

	public static class BreakSpeed implements ModifyBlockBreakingSpeedEvent {
		@Override
		public float modify(float breakSpeed, PlayerEntity player, BlockState state, BlockView world, BlockPos pos) {
			if (canActivate(player, player.getMainHandStack(), state)) {
				Set<BlockPos> ores = gatherOres(new HashSet<>(), player.getEntityWorld(), new BlockPos.Mutable().set(pos), state.getBlock());
				if (isValid(ores, player.getMainHandStack())) {
					float mineOreVeinsSpeed = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MINE_ORE_VEINS, player.getRandom(), player.getMainHandStack(), 0);
					return MathHelper.lerp(Math.min(1, ores.size() / 12F), mineOreVeinsSpeed, mineOreVeinsSpeed * 0.1F);
				}
			}
			return 1;
		}
	}

	public static class MineOres implements PlayerBlockBreakEvents.Before {
		@Override
		public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
			ItemStack stack = player.getMainHandStack();
			if (canActivate(player, stack, state)) {
				Set<BlockPos> ores = gatherOres(new HashSet<>(), world, new BlockPos.Mutable().set(pos), state.getBlock());
				if (isValid(ores, stack)) {
					BlockState replace = getBaseBlock(state).getDefaultState();
					List<ItemStack> drops = new ArrayList<>();
					ores.forEach(ore -> {
						BlockState oreState = world.getBlockState(ore);
						drops.addAll(Block.getDroppedStacks(oreState, (ServerWorld) world, ore, world.getBlockEntity(ore), player, stack));
						oreState.onStacksDropped((ServerWorld) world, player.getBlockPos(), stack, true);
						world.breakBlock(ore, false);
						world.setBlockState(ore, replace);
					});
					world.playSound(null, pos, ModSoundEvents.BLOCK_ORE_EXTRACT, SoundCategory.BLOCKS, 1, 1);
					if (!drops.isEmpty()) {
						EnchancementUtil.mergeItemEntities(drops.stream().map(drop -> new ItemEntity(world, player.getX(), player.getY() + 0.5, player.getZ(), drop)).collect(Collectors.toList())).forEach(world::spawnEntity);
					}
					stack.damage(ores.size(), player, EquipmentSlot.MAINHAND);
					return false;
				}
			}
			return true;
		}
	}
}
