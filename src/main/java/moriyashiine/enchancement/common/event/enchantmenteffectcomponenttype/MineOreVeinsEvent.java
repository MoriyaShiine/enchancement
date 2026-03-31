/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.tag.ModBlockTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.util.enchantment.BaseBlock;
import moriyashiine.strawberrylib.api.event.ModifyDestroyProgressEvent;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MineOreVeinsEvent {
	public static Set<BlockPos> gatherOres(Set<BlockPos> ores, Level level, BlockPos.MutableBlockPos pos, Block original) {
		if (ores.size() < ModConfig.maxMineOreVeinsBlocks) {
			int originalX = pos.getX(), originalY = pos.getY(), originalZ = pos.getZ();
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						if (level.getWorldBorder().isWithinBounds(pos.set(originalX + x, originalY + y, originalZ + z))) {
							BlockState state = level.getBlockState(pos);
							if (state.is(ConventionalBlockTags.ORES) && !ores.contains(pos) && state.getBlock() == original) {
								ores.add(pos.immutable());
								gatherOres(ores, level, pos, original);
							}
						}
					}
				}
			}
		}
		return ores;
	}

	public static boolean canActivate(Player player, ItemStack stack, BlockState state) {
		return !player.isShiftKeyDown() && EnchantmentHelper.has(stack, ModEnchantmentEffectComponentTypes.MINE_ORE_VEINS) && state.is(ConventionalBlockTags.ORES) && player.hasCorrectToolForDrops(state);
	}

	public static boolean isValid(Set<BlockPos> ores, ItemStack stack) {
		if (!stack.isDamageableItem() || stack.getDamageValue() + ores.size() <= stack.getMaxDamage()) {
			return !ores.isEmpty() && ores.size() <= ModConfig.maxMineOreVeinsBlocks;
		}
		return false;
	}

	private static Block getBaseBlock(BlockState state) {
		BaseBlock baseBlock = BaseBlock.BLOCK_MAP.get(state.getBlock());
		if (baseBlock != null) {
			return baseBlock.base();
		}
		if (state.is(ModBlockTags.NETHER_ORES)) {
			return Blocks.NETHERRACK;
		} else if (state.is(ModBlockTags.END_ORES)) {
			return Blocks.END_STONE;
		} else if (state.is(ConventionalBlockTags.ORES_IN_GROUND_DEEPSLATE)) {
			return Blocks.DEEPSLATE;
		}
		return Blocks.STONE;
	}

	public static class BreakSpeed implements ModifyDestroyProgressEvent {
		@Override
		public float modify(Player player, BlockState state, BlockGetter level, BlockPos pos) {
			if (canActivate(player, player.getMainHandItem(), state)) {
				Set<BlockPos> ores = gatherOres(new HashSet<>(), player.level(), new BlockPos.MutableBlockPos().set(pos), state.getBlock());
				if (isValid(ores, player.getMainHandItem())) {
					float mineOreVeinsSpeed = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MINE_ORE_VEINS, player.getRandom(), player.getMainHandItem(), 0);
					return Mth.lerp(Math.min(1, ores.size() / 12F), mineOreVeinsSpeed, mineOreVeinsSpeed * 0.1F);
				}
			}
			return 1;
		}
	}

	public static class MineOres implements PlayerBlockBreakEvents.Before {
		@Override
		public boolean beforeBlockBreak(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
			ItemStack stack = player.getMainHandItem();
			if (canActivate(player, stack, state)) {
				Set<BlockPos> ores = gatherOres(new HashSet<>(), level, new BlockPos.MutableBlockPos().set(pos), state.getBlock());
				if (isValid(ores, stack)) {
					BlockState replace = getBaseBlock(state).defaultBlockState();
					List<ItemStack> drops = new ArrayList<>();
					ores.forEach(ore -> {
						BlockState oreState = level.getBlockState(ore);
						drops.addAll(Block.getDrops(oreState, (ServerLevel) level, ore, level.getBlockEntity(ore), player, stack));
						oreState.spawnAfterBreak((ServerLevel) level, player.blockPosition(), stack, true);
						level.destroyBlock(ore, false);
						level.setBlockAndUpdate(ore, replace);
					});
					level.playSound(null, pos, ModSoundEvents.BLOCK_ORE_EXTRACT, SoundSource.BLOCKS, 1, 1);
					if (!drops.isEmpty()) {
						EnchancementUtil.mergeItemEntities(drops.stream().map(drop -> new ItemEntity(level, player.getX(), player.getY() + 0.5, player.getZ(), drop)).collect(Collectors.toList())).forEach(level::addFreshEntity);
					}
					stack.hurtAndBreak(ores.size(), player, EquipmentSlot.MAINHAND);
					return false;
				}
			}
			return true;
		}
	}
}
