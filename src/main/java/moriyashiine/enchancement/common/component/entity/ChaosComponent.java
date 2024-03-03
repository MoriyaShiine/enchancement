/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class ChaosComponent implements Component {
	private ItemStack originalStack = ItemStack.EMPTY;

	@Override
	public void readFromNbt(NbtCompound tag) {
		originalStack = ItemStack.fromNbt(tag.getCompound("OriginalStack"));
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		tag.put("OriginalStack", originalStack.writeNbt(new NbtCompound()));
	}

	public ItemStack getOriginalStack() {
		return originalStack;
	}

	public void setOriginalStack(ItemStack originalStack) {
		this.originalStack = originalStack;
	}
}
