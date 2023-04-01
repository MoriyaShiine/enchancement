/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments.integration.architectury;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.fabric.RegistrarManagerImpl;
import moriyashiine.enchancement.common.util.RemovedRegistryEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RegistrarManagerImpl.RegistrarImpl.class, remap = false)
public abstract class RegistrarManagerImplRegistrarImplMixin<T> implements Registrar<T> {
	@Shadow
	private Registry<T> delegate;

	@Inject(method = "contains", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(Identifier id, CallbackInfoReturnable<Boolean> cir) {
		if (delegate == Registries.ENCHANTMENT) {
			if (RemovedRegistryEntry.getFromId(id) != null) {
				cir.setReturnValue(true);
			}
		}
	}
}
