/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.torch;

import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CrossbowItem.class)
public interface CrossbowItemAccessor {
	@Invoker("getPullProgress")
	static float enchancement$getPullProgress(int useTicks, ItemStack stack) {
		throw new UnsupportedOperationException();
	}
}
