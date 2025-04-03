/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.block;

import moriyashiine.enchancement.common.init.ModBlockComponents;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class ChiseledBookshelfComponent implements AutoSyncedComponent {
	private final ChiseledBookshelfBlockEntity obj;
	private boolean hasEnchantments = false;

	public ChiseledBookshelfComponent(ChiseledBookshelfBlockEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup wrapperLookup) {
		hasEnchantments = tag.getBoolean("HasEnchantments", false);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup wrapperLookup) {
		tag.putBoolean("HasEnchantments", hasEnchantments);
	}

	public void sync() {
		ModBlockComponents.CHISELED_BOOKSHELF.sync(obj);
	}

	public boolean hasEnchantments() {
		return hasEnchantments;
	}

	public void update() {
		for (ItemStack stack : obj) {
			if (!EnchantmentHelper.getEnchantments(stack).isEmpty()) {
				hasEnchantments = true;
				return;
			}
		}
		hasEnchantments = false;
	}
}
