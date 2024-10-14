/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryOwner;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unchecked")
@Mixin(RegistryEntry.Reference.class)
public class RegistryEntryReferenceMixin<T> {
	@Shadow
	@Final
	private RegistryEntryOwner<T> owner;

	@Shadow
	private @Nullable RegistryKey<T> registryKey;

	@Shadow
	private @Nullable T value;

	@Inject(method = "registryKey", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantmentsKey(CallbackInfoReturnable<RegistryKey<T>> cir) {
		if (isEnchantment() && registryKey == null) {
			cir.setReturnValue((RegistryKey<T>) ModEnchantments.EMPTY_KEY);
		}
	}

	@Inject(method = "value", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantmentsValue(CallbackInfoReturnable<T> cir) {
		if (isEnchantment() && value == null) {
			cir.setReturnValue((T) ModEnchantments.EMPTY);
		}
	}

	@Unique
	private boolean isEnchantment() {
		return EnchancementUtil.ENCHANTMENT_REGISTRY != null && owner.ownerEquals((RegistryEntryOwner<T>) EnchancementUtil.ENCHANTMENT_REGISTRY.getEntryOwner());
	}
}
