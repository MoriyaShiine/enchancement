/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RegistryEntryReferenceArgumentType.class)
public class RegistryEntryReferenceArgumentTypeMixin {
	@Shadow
	@Final
	public static Dynamic2CommandExceptionType NOT_FOUND_EXCEPTION;

	@ModifyReturnValue(method = "getEnchantment", at = @At("RETURN"))
	private static RegistryEntry.Reference<Enchantment> enchancement$disableDisallowedEnchantments(RegistryEntry.Reference<Enchantment> original) throws CommandSyntaxException {
		if (original.matchesKey(ModEnchantments.EMPTY_KEY)) {
			throw NOT_FOUND_EXCEPTION.create(original.registryKey().getValue(), original.registryKey().getRegistry());
		}
		return original;
	}
}
