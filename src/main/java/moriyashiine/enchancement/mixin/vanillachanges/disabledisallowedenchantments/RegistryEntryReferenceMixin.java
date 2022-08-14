package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.util.RemovedRegistryEntry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
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
	@Final
	private Registry<T> registry;

	@Shadow
	@Nullable
	private RegistryKey<T> registryKey;

	@Inject(method = "value", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(CallbackInfoReturnable<T> cir) {
		if (registryKey != null && registry == Registry.ENCHANTMENT) {
			RemovedRegistryEntry removedEntry = RemovedRegistryEntry.getFromId(registryKey.getValue());
			if (removedEntry != null) {
				cir.setReturnValue((T) removedEntry.enchantment());
			}
		}
	}
}
