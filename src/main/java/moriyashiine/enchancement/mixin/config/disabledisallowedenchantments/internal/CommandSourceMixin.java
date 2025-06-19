/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.stream.Stream;

@Mixin(CommandSource.class)
public interface CommandSourceMixin {
	@ModifyVariable(method = "suggestIdentifiers(Ljava/util/stream/Stream;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), argsOnly = true)
	private static Stream<Identifier> enchancement$disableDisallowedEnchantments(Stream<Identifier> value) {
		return value.filter(key -> !key.equals(ModEnchantments.EMPTY_KEY.getValue()));
	}
}
