/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SimpleRegistry.class)
public class SimpleRegistryMixin<T> {
	@Shadow
	@Final
	RegistryKey<? extends Registry<T>> key;

	@ModifyExpressionValue(method = "freeze", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
	private boolean enchancement$disableDisallowedEnchantments(boolean original) {
		return original || key.equals(RegistryKeys.ENCHANTMENT);
	}
}
