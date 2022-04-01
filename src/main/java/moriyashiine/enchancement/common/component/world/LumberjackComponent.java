package moriyashiine.enchancement.common.component.world;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LumberjackComponent implements ServerTickingComponent {
	private final World obj;
	private final List<Tree> treesToCut = new ArrayList<>();

	public LumberjackComponent(World obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList treesToCut = tag.getList("TreesToCut", NbtElement.COMPOUND_TYPE);
		for (int i = 0; i < treesToCut.size(); i++) {
			this.treesToCut.add(Tree.deserialize(treesToCut.getCompound(i)));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList treesToCut = new NbtList();
		treesToCut.addAll(this.treesToCut.stream().map(Tree::serialize).toList());
		tag.put("TreesToCut", treesToCut);
	}

	@Override
	public void serverTick() {
		for (int i = treesToCut.size() - 1; i >= 0; i--) {
			Tree tree = treesToCut.get(i);
			BlockPos pos = tree.logs.remove(tree.logs.size() - 1);
			tree.drops.addAll(Block.getDroppedStacks(obj.getBlockState(pos), (ServerWorld) obj, pos, obj.getBlockEntity(pos)));
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

		public Tree(List<BlockPos> logs, BlockPos originalPos) {
			this.logs = logs;
			this.drops = new ArrayList<>();
			this.originalPos = originalPos;
		}

		public NbtCompound serialize() {
			NbtCompound compound = new NbtCompound();
			compound.putLongArray("Logs", logs.stream().map(BlockPos::asLong).toList());
			NbtList drops = new NbtList();
			this.drops.forEach(stack -> drops.add(stack.writeNbt(new NbtCompound())));
			compound.put("Drops", drops);
			compound.putLong("OriginalPos", originalPos.asLong());
			return compound;
		}

		public static Tree deserialize(NbtCompound compound) {
			Tree tree = new Tree(Arrays.stream(compound.getLongArray("Logs")).mapToObj(BlockPos::fromLong).collect(Collectors.toList()), BlockPos.fromLong(compound.getLong("OriginalPos")));
			NbtList drops = compound.getList("Drops", NbtElement.COMPOUND_TYPE);
			for (int i = 0; i < drops.size(); i++) {
				tree.drops.add(ItemStack.fromNbt(drops.getCompound(i)));
			}
			return tree;
		}
	}
}
