/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.block;

import moriyashiine.enchancement.common.init.ModBlockComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.ArrayList;
import java.util.List;

public class ChiseledBookshelfComponent implements AutoSyncedComponent {
	private static final Item PLACEHOLDER = Items.BEDROCK;

	private final ChiseledBookShelfBlockEntity obj;
	private final List<ItemStack> stacks = new ArrayList<>();
	private boolean hasEnchantments = false;

	public ChiseledBookshelfComponent(ChiseledBookShelfBlockEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		stacks.clear();
		for (ItemStack stack : input.read("Stacks", ItemStack.CODEC.listOf()).orElse(List.of())) {
			stacks.add(stack.is(PLACEHOLDER) ? ItemStack.EMPTY : stack);
		}
		hasEnchantments = input.getBooleanOr("HasEnchantments", false);
	}

	@Override
	public void writeData(ValueOutput output) {
		List<ItemStack> stacks = this.stacks;
		stacks.replaceAll(stack -> stack.isEmpty() ? PLACEHOLDER.getDefaultInstance() : stack);
		output.store("Stacks", ItemStack.CODEC.listOf(), stacks);
		output.putBoolean("HasEnchantments", hasEnchantments);
	}

	public void sync() {
		ModBlockComponents.CHISELED_BOOKSHELF.sync(obj);
	}

	public List<ItemStack> getStacks() {
		return stacks;
	}

	public boolean hasEnchantments() {
		return hasEnchantments;
	}

	public void update() {
		stacks.clear();
		hasEnchantments = false;
		for (ItemStack stack : obj) {
			stacks.add(stack.copy());
			if (!hasEnchantments && !EnchantmentHelper.getEnchantmentsForCrafting(stack).isEmpty()) {
				hasEnchantments = true;
			}
		}
	}
}
