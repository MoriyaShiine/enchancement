/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

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

	@Inject(method = "hasKeyAndValue", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantments(CallbackInfoReturnable<Boolean> cir) {
		validate();
	}

	@Inject(method = "registryKey", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantmentsKey(CallbackInfoReturnable<RegistryKey<T>> cir) {
		validate();
	}

	@Inject(method = "value", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantmentsValue(CallbackInfoReturnable<T> cir) {
		validate();
	}

	@Unique
	private void validate() {
		if (EnchancementUtil.ENCHANTMENT_REGISTRY_OWNER != null && owner.ownerEquals((RegistryEntryOwner<T>) EnchancementUtil.ENCHANTMENT_REGISTRY_OWNER)) {
			if (registryKey == null || value == null) {
				registryKey = (RegistryKey<T>) ModEnchantments.EMPTY_KEY;
				value = (T) ModEnchantments.EMPTY;
			}
		}
	}
}
