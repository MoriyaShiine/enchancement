/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.util.RemovedRegistryEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryOwner;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unchecked")
@Mixin(RegistryEntry.Reference.class)
public class RegistryEntryReferenceMixin<T> {
	@Shadow
	@Nullable
	private RegistryKey<T> registryKey;

	@Shadow
	@Final
	private RegistryEntryOwner<T> owner;

	@Inject(method = "value", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(CallbackInfoReturnable<T> cir) {
		if (registryKey != null && owner == Registries.ENCHANTMENT.getEntryOwner()) {
			RemovedRegistryEntry removedEntry = RemovedRegistryEntry.getFromId(registryKey.getValue());
			if (removedEntry != null) {
				cir.setReturnValue((T) removedEntry.enchantment());
			}
		}
	}
}
