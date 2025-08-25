/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FellTreesComponent implements ServerTickingComponent {
	private final World obj;
	private final List<Tree> treesToCut = new ArrayList<>();

	public FellTreesComponent(World obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		treesToCut.clear();
		for (Tree tree : readView.read("TreesToCut", Tree.CODEC.listOf()).orElse(List.of())) {
			treesToCut.add(new Tree(new ArrayList<>(tree.logs), new ArrayList<>(tree.drops), tree.originalPos, tree.stack.copy()));
		}
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.put("TreesToCut", Tree.CODEC.listOf(), treesToCut);
	}

	@Override
	public void serverTick() {
		for (int i = treesToCut.size() - 1; i >= 0; i--) {
			Tree tree = treesToCut.get(i);
			if (tree.logs.isEmpty()) {
				EnchancementUtil.mergeItemEntities(tree.drops.stream().map(drop -> new ItemEntity(obj, tree.originalPos.getX() + 0.5, tree.originalPos.getY() + 0.5, tree.originalPos.getZ() + 0.5, drop)).collect(Collectors.toList())).forEach(obj::spawnEntity);
				treesToCut.remove(i);
				continue;
			}
			BlockPos pos = tree.logs.removeLast();
			tree.drops.addAll(Block.getDroppedStacks(obj.getBlockState(pos), (ServerWorld) obj, pos, obj.getBlockEntity(pos), null, tree.stack));
			obj.breakBlock(pos, false);
		}
	}

	public void addTree(Tree tree) {
		treesToCut.add(tree);
	}

	public record Tree(List<BlockPos> logs, List<ItemStack> drops, BlockPos originalPos, ItemStack stack) {
		public static final Codec<Tree> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						BlockPos.CODEC.listOf().fieldOf("logs").forGetter(Tree::logs),
						ItemStack.CODEC.listOf().fieldOf("drops").forGetter(Tree::drops),
						BlockPos.CODEC.fieldOf("original_pos").forGetter(Tree::originalPos),
						ItemStack.CODEC.fieldOf("stack").forGetter(Tree::stack))
				.apply(instance, Tree::new));

		public static Tree of(List<BlockPos> logs, BlockPos originalPos, ItemStack stack) {
			return new Tree(logs, new ArrayList<>(), originalPos, stack.copy());
		}
	}
}
