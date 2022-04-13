package moriyashiine.enchancement.mixin.molten;

import moriyashiine.enchancement.client.packet.AddMoltenParticlesPacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public class BlockMixin {
	@Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
	private static void enchancement$molten(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> cir) {
		if (entity != null && entity.isSneaking()) {
			return;
		}
		if (EnchantmentHelper.getLevel(ModEnchantments.MOLTEN, stack) > 0) {
			List<ItemStack> drops = cir.getReturnValue();
			if (!drops.isEmpty()) {
				for (int i = 0; i < drops.size(); i++) {
					Pair<ItemStack, Float> smelted = getSmeltedStack(world, drops.get(i));
					if (smelted != null) {
						PlayerLookup.tracking(world, pos).forEach(foundPlayer -> AddMoltenParticlesPacket.send(foundPlayer, pos));
						world.playSound(null, pos, ModSoundEvents.BLOCK_GENERIC_SMELT, SoundCategory.BLOCKS, 1, 1);
						drops.set(i, smelted.getLeft());
						AbstractFurnaceBlockEntityAccessor.enchancement$dropExperience(world, Vec3d.of(pos), 1, smelted.getRight());
					}
				}
			}
		}
	}

	@Unique
	private static Pair<ItemStack, Float> getSmeltedStack(ServerWorld world, ItemStack stack) {
		for (SmeltingRecipe recipe : world.getRecipeManager().listAllOfType(RecipeType.SMELTING)) {
			for (Ingredient ingredient : recipe.getIngredients()) {
				if (ingredient.test(stack)) {
					return new Pair<>(new ItemStack(recipe.getOutput().getItem(), recipe.getOutput().getCount() * stack.getCount()), recipe.getExperience());
				}
			}
		}
		return null;
	}
}
