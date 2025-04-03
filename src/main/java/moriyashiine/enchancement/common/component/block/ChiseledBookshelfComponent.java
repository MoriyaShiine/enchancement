/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.block;

import moriyashiine.enchancement.common.init.ModBlockComponents;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.ArrayList;
import java.util.List;

public class ChiseledBookshelfComponent implements AutoSyncedComponent {
	private static final Item PLACEHOLDER = Items.BEDROCK;

	private final ChiseledBookshelfBlockEntity obj;
	private final List<ItemStack> stacks = new ArrayList<>();
	private boolean hasEnchantments = false;

	public ChiseledBookshelfComponent(ChiseledBookshelfBlockEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup wrapperLookup) {
		stacks.clear();
		for (ItemStack stack : tag.get("Stacks", ItemStack.CODEC.listOf(), wrapperLookup.getOps(NbtOps.INSTANCE)).orElse(List.of())) {
			stacks.add(stack.isOf(PLACEHOLDER) ? ItemStack.EMPTY : stack);
		}
		hasEnchantments = tag.getBoolean("HasEnchantments", false);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup wrapperLookup) {
		List<ItemStack> stacks = this.stacks;
		stacks.replaceAll(stack -> stack.isEmpty() ? PLACEHOLDER.getDefaultStack() : stack);
		tag.put("Stacks", ItemStack.CODEC.listOf(), wrapperLookup.getOps(NbtOps.INSTANCE), stacks);
		tag.putBoolean("HasEnchantments", hasEnchantments);
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
			if (!hasEnchantments && !EnchantmentHelper.getEnchantments(stack).isEmpty()) {
				hasEnchantments = true;
			}
		}
	}
}
