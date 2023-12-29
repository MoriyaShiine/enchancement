/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.brimstone;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract void removeSubNbt(String key);

	@Inject(method = "removeSubNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;remove(Ljava/lang/String;)V"))
	private void enchancement$brimstone(String key, CallbackInfo ci) {
		if (key.equals(ItemStack.ENCHANTMENTS_KEY)) {
			removeSubNbt("BrimstoneUUID");
		}
	}
}
