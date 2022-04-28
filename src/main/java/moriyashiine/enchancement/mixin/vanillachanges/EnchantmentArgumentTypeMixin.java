package moriyashiine.enchancement.mixin.vanillachanges;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.minecraft.command.argument.EnchantmentArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EnchantmentArgumentType.class)
public class EnchantmentArgumentTypeMixin {
	@Shadow
	@Final
	public static DynamicCommandExceptionType UNKNOWN_ENCHANTMENT_EXCEPTION;

	@Inject(method = "parse(Lcom/mojang/brigadier/StringReader;)Lnet/minecraft/enchantment/Enchantment;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void enchancement$emptyEnchantmentHack(StringReader stringReader, CallbackInfoReturnable<Enchantment> cir, Identifier identifier) throws CommandSyntaxException {
		if (cir.getReturnValue() == ModEnchantments.EMPTY) {
			throw UNKNOWN_ENCHANTMENT_EXCEPTION.create(identifier);
		}
	}
}
