/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.brimstone;

import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(CrossbowItem.class)
public interface CrossbowItemAccessor {
	@Invoker("getProjectiles")
	static List<ItemStack> enchancement$getProjectiles(ItemStack crossbow) {
		throw new UnsupportedOperationException();
	}
}
