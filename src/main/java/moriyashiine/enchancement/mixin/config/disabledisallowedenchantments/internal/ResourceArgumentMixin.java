/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ResourceArgument.class)
public class ResourceArgumentMixin {
	@Shadow
	@Final
	public static Dynamic2CommandExceptionType ERROR_UNKNOWN_RESOURCE;

	@ModifyReturnValue(method = "getEnchantment", at = @At("RETURN"))
	private static Holder.Reference<Enchantment> enchancement$disableDisallowedEnchantments(Holder.Reference<Enchantment> original) throws CommandSyntaxException {
		if (original.is(ModEnchantments.EMPTY_KEY)) {
			throw ERROR_UNKNOWN_RESOURCE.create(original.key().identifier(), original.key().registry());
		}
		return original;
	}
}
