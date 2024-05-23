/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.molten;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.client.payload.AddMoltenParticlesPayload;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.init.ModTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.accessor.AbstractFurnaceBlockEntityAccessor;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
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

@Mixin(Block.class)
public class BlockMixin {
	@ModifyReturnValue(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"))
	private static List<ItemStack> enchancement$molten(List<ItemStack> original, BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack) {
		if (entity != null && entity.isSneaking()) {
			return original;
		}
		if (EnchancementUtil.hasEnchantment(ModEnchantments.MOLTEN, stack) && !original.isEmpty()) {
			original = new ArrayList<>(original);
			boolean smeltsSelf = state.isIn(ModTags.Blocks.SMELTS_SELF);
			for (int i = 0; i < original.size(); i++) {
				Pair<ItemStack, Float> smelted = getSmeltedStack(world, smeltsSelf ? new ItemStack(state.getBlock()) : original.get(i));
				if (smelted != null) {
					PlayerLookup.tracking(world, pos).forEach(foundPlayer -> AddMoltenParticlesPayload.send(foundPlayer, pos));
					world.playSound(null, pos, ModSoundEvents.BLOCK_GENERIC_SMELT, SoundCategory.BLOCKS, 1, 1);
					original.set(i, smelted.getLeft());
					AbstractFurnaceBlockEntityAccessor.enchancement$dropExperience(world, entity != null && EnchancementUtil.hasEnchantment(ModEnchantments.EXTRACTING, stack) ? entity.getPos() : Vec3d.of(pos), 1, smelted.getRight());
				}
			}
		}
		return original;
	}

	@Unique
	private static Pair<ItemStack, Float> getSmeltedStack(ServerWorld world, ItemStack stack) {
		for (RecipeEntry<SmeltingRecipe> recipe : world.getRecipeManager().listAllOfType(RecipeType.SMELTING)) {
			for (Ingredient ingredient : recipe.value().getIngredients()) {
				if (ingredient.test(stack)) {
					return new Pair<>(new ItemStack(recipe.value().getResult(world.getRegistryManager()).getItem(), recipe.value().getResult(world.getRegistryManager()).getCount() * stack.getCount()), recipe.value().getExperience());
				}
			}
		}
		return null;
	}
}
