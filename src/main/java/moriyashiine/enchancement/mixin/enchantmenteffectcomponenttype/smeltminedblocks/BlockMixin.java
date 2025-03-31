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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(Block.class)
public class BlockMixin {
	@ModifyReturnValue(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"))
	private static List<ItemStack> enchancement$smeltMinedBlocks(List<ItemStack> original, BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack) {
		if (entity != null && entity.isSneaking()) {
			return original;
		}
		if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.SMELT_MINED_BLOCKS)) {
			if (state.isIn(ModBlockTags.SMELTS_SELF)) {
				ItemStack smelted = smeltBlock(world, pos, entity, state.getBlock().asItem().getDefaultStack());
				if (!smelted.isEmpty()) {
					return List.of(smelted);
				}
			} else if (!original.isEmpty()) {
				original = new ArrayList<>(original);
				for (int i = 0; i < original.size(); i++) {
					ItemStack smelted = smeltBlock(world, pos, entity, original.get(i));
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
	private static ItemStack smeltBlock(ServerWorld world, BlockPos pos, @Nullable Entity entity, ItemStack stack) {
		Pair<ItemStack, Float> smelted = getSmeltedStack(world, stack);
		if (smelted != null) {
			PlayerLookup.tracking(world, pos).forEach(foundPlayer -> AddMoltenParticlesPayload.send(foundPlayer, pos));
			world.playSound(null, pos, ModSoundEvents.BLOCK_GENERIC_SMELT, SoundCategory.BLOCKS, 1, 1);
			FurnaceBlockEntity.dropExperience(world, entity != null && EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.MINE_ORE_VEINS) ? entity.getPos() : Vec3d.ofCenter(pos), 1, smelted.getRight());
			return smelted.getLeft();
		}
		return ItemStack.EMPTY;
	}

	@Unique
	private static Pair<ItemStack, Float> getSmeltedStack(ServerWorld world, ItemStack stack) {
		SingleStackRecipeInput input = new SingleStackRecipeInput(stack);
		Optional<RecipeEntry<SmeltingRecipe>> firstMatch = ServerRecipeManager.createCachedMatchGetter(RecipeType.SMELTING).getFirstMatch(input, world);
		if (firstMatch.isPresent()) {
			SmeltingRecipe recipe = firstMatch.get().value();
			ItemStack output = recipe.craft(input, world.getRegistryManager());
			return new Pair<>(new ItemStack(output.getItem(), output.getCount() * stack.getCount()), recipe.getExperience());
		}
		return null;
	}
}
