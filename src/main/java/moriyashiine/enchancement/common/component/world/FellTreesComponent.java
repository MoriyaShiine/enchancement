/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.world;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FellTreesComponent implements ServerTickingComponent {
	private final World obj;
	private final List<Tree> treesToCut = new ArrayList<>();

	public FellTreesComponent(World obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		NbtList treesToCut = tag.getList("TreesToCut", NbtElement.COMPOUND_TYPE);
		for (int i = 0; i < treesToCut.size(); i++) {
			this.treesToCut.add(Tree.deserialize(registryLookup, treesToCut.getCompound(i)));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		NbtList treesToCut = new NbtList();
		treesToCut.addAll(this.treesToCut.stream().map(tree -> tree.serialize(registryLookup)).toList());
		tag.put("TreesToCut", treesToCut);
	}

	@Override
	public void serverTick() {
		for (int i = treesToCut.size() - 1; i >= 0; i--) {
			Tree tree = treesToCut.get(i);
			BlockPos pos = tree.logs.removeLast();
			tree.drops.addAll(Block.getDroppedStacks(obj.getBlockState(pos), (ServerWorld) obj, pos, obj.getBlockEntity(pos), null, tree.stack));
			obj.breakBlock(pos, false);
			if (tree.logs.isEmpty()) {
				EnchancementUtil.mergeItemEntities(tree.drops.stream().map(drop -> new ItemEntity(obj, tree.originalPos.getX() + 0.5, tree.originalPos.getY() + 0.5, tree.originalPos.getZ() + 0.5, drop)).collect(Collectors.toList())).forEach(obj::spawnEntity);
				treesToCut.remove(i);
			}
		}
	}

	public void addTree(Tree tree) {
		treesToCut.add(tree);
	}

	public static class Tree {
		public final List<BlockPos> logs;
		public final List<ItemStack> drops;
		public final BlockPos originalPos;
		public final ItemStack stack;

		public Tree(List<BlockPos> logs, BlockPos originalPos, ItemStack stack) {
			this.logs = logs;
			this.drops = new ArrayList<>();
			this.originalPos = originalPos;
			this.stack = stack.copy();
		}

		public NbtCompound serialize(RegistryWrapper.WrapperLookup registryLookup) {
			NbtCompound compound = new NbtCompound();
			compound.putLongArray("Logs", logs.stream().map(BlockPos::asLong).toList());
			NbtList drops = new NbtList();
			this.drops.forEach(stack -> {
				if (!stack.isEmpty()) {
					drops.add(stack.toNbt(registryLookup));
				}
			});
			compound.put("Drops", drops);
			compound.putLong("OriginalPos", originalPos.asLong());
			compound.put("Stack", stack.toNbt(registryLookup));
			return compound;
		}

		public static Tree deserialize(RegistryWrapper.WrapperLookup registryLookup, NbtCompound compound) {
			Tree tree = new Tree(Arrays.stream(compound.getLongArray("Logs")).mapToObj(BlockPos::fromLong).collect(Collectors.toList()), BlockPos.fromLong(compound.getLong("OriginalPos")), ItemStack.fromNbtOrEmpty(registryLookup, compound.getCompound("Stack")));
			NbtList drops = compound.getList("Drops", NbtElement.COMPOUND_TYPE);
			for (int i = 0; i < drops.size(); i++) {
				ItemStack.fromNbt(registryLookup, drops.getCompound(i)).ifPresent(tree.drops::add);
			}
			return tree;
		}
	}
}
