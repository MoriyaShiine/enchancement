package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EnchantmentArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mixin(EnchantmentArgumentType.class)
public class EnchantmentArgumentTypeMixin {
	@Inject(method = "listSuggestions", at = @At("HEAD"), cancellable = true)
	private <S> void enchancement$disableDisallowedEnchantments(CommandContext<S> context, SuggestionsBuilder builder, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
		if (!Enchancement.getConfig().allowedEnchantmentIdentifiers.isEmpty()) {
			Set<Identifier> identifiers = new HashSet<>();
			for (Enchantment enchantment : Registry.ENCHANTMENT) {
				Identifier identifier = Registry.ENCHANTMENT.getId(enchantment);
				if (!Enchancement.getConfig().isEnchantmentDisallowed(identifier)) {
					identifiers.add(identifier);
				}
			}
			cir.setReturnValue(CommandSource.suggestIdentifiers(identifiers, builder));
		}
	}

	@Inject(method = "parse(Lcom/mojang/brigadier/StringReader;)Lnet/minecraft/enchantment/Enchantment;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/Registry;getOrEmpty(Lnet/minecraft/util/Identifier;)Ljava/util/Optional;"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void enchancement$disableDisallowedEnchantments(StringReader stringReader, CallbackInfoReturnable<Enchantment> cir, Identifier identifier) throws CommandSyntaxException {
		if (Enchancement.getConfig().isEnchantmentDisallowed(identifier)) {
			throw EnchantmentArgumentType.UNKNOWN_ENCHANTMENT_EXCEPTION.create(identifier);
		}
	}
}
