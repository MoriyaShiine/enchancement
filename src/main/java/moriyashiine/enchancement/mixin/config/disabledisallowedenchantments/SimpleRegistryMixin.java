/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments;

import com.mojang.serialization.Lifecycle;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntryOwner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> {
	@Shadow
	public abstract RegistryEntryOwner<T> getEntryOwner();

	@Inject(method = "<init>(Lnet/minecraft/registry/RegistryKey;Lcom/mojang/serialization/Lifecycle;Z)V", at = @At("TAIL"))
	private void enchancement$disableDisallowedEnchantments(RegistryKey<?> key, Lifecycle lifecycle, boolean intrusive, CallbackInfo ci) {
		if (key.equals(RegistryKeys.ENCHANTMENT)) {
			EnchancementUtil.ENCHANTMENT_REGISTRY_OWNER = getEntryOwner();
		}
	}
}
