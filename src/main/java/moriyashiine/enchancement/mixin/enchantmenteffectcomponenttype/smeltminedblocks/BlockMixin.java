/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.smeltminedblocks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.client.payload.AddMoltenParticlesPayload;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.tag.ModBlockTags;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(Block.class)
public class BlockMixin {
	@ModifyReturnValue(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemInstance;)Ljava/util/List;", at = @At("RETURN"))
	private static List<ItemStack> enchancement$smeltMinedBlocks(List<ItemStack> original, BlockState state, ServerLevel level, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity breaker, ItemInstance tool) {
		if (breaker != null && breaker.isShiftKeyDown()) {
			return original;
		}
		ItemEnchantments enchantments = tool.get(DataComponents.ENCHANTMENTS);
		if (enchantments != null && enchantments.keySet().stream().anyMatch(enchantment -> enchantment.value().effects().has(ModEnchantmentEffectComponentTypes.SMELT_MINED_BLOCKS))) {
			if (state.is(ModBlockTags.SMELTS_SELF)) {
				ItemStack smelted = smeltBlock(level, pos, breaker, state.getBlock().asItem().getDefaultInstance());
				if (!smelted.isEmpty()) {
					return List.of(smelted);
				}
			} else if (!original.isEmpty()) {
				original = new ArrayList<>(original);
				for (int i = 0; i < original.size(); i++) {
					ItemStack smelted = smeltBlock(level, pos, breaker, original.get(i));
					if (!smelted.isEmpty()) {
						original.set(i, smelted);
					}
				}
				return List.copyOf(original);
			}
		}
		return original;
	}

	@Unique
	private static ItemStack smeltBlock(ServerLevel level, BlockPos pos, @Nullable Entity entity, ItemStack stack) {
		Tuple<ItemStack, Float> smelted = getSmeltedStack(level, stack);
		if (smelted != null) {
			PlayerLookup.tracking(level, pos).forEach(foundPlayer -> AddMoltenParticlesPayload.send(foundPlayer, pos));
			level.playSound(null, pos, ModSoundEvents.BLOCK_GENERIC_SMELT, SoundSource.BLOCKS, 1, 1);
			FurnaceBlockEntity.createExperience(level, entity != null && EnchantmentHelper.has(stack, ModEnchantmentEffectComponentTypes.MINE_ORE_VEINS) ? entity.position() : Vec3.atCenterOf(pos), 1, smelted.getB());
			return smelted.getA();
		}
		return ItemStack.EMPTY;
	}

	@Unique
	private static Tuple<ItemStack, Float> getSmeltedStack(ServerLevel level, ItemStack stack) {
		SingleRecipeInput input = new SingleRecipeInput(stack);
		Optional<RecipeHolder<SmeltingRecipe>> firstMatch = RecipeManager.createCheck(RecipeType.SMELTING).getRecipeFor(input, level);
		if (firstMatch.isPresent()) {
			SmeltingRecipe recipe = firstMatch.get().value();
			ItemStack output = recipe.assemble(input);
			return new Tuple<>(new ItemStack(output.getItem(), output.getCount() * stack.getCount()), recipe.experience());
		}
		return null;
	}
}
