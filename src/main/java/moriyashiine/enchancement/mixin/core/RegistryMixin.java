package moriyashiine.enchancement.mixin.core;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Registry.class)
public class RegistryMixin {
	@Shadow
	@Final
	public static Registry<Enchantment> ENCHANTMENT;

	@Inject(method = "register(Lnet/minecraft/util/registry/Registry;Lnet/minecraft/util/registry/RegistryKey;Ljava/lang/Object;)Ljava/lang/Object;", at = @At("HEAD"), cancellable = true)
	private static <V, T extends V> void enchancement$disableDisallowedEnchantments(Registry<V> registry, RegistryKey<V> key, T entry, CallbackInfoReturnable<T> cir) {
		if (registry == ENCHANTMENT) {
			if (!Enchancement.getConfig().allowedEnchantmentIdentifiers.isEmpty() && !Enchancement.getConfig().allowedEnchantmentIdentifiers.contains(key.getValue())) {
				cir.setReturnValue(entry);
			}
		}
	}

	@SuppressWarnings({"ConstantConditions", "unchecked"})
	@Inject(method = "getOrEmpty(Lnet/minecraft/util/Identifier;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
	private <T> void enchancement$ignoreDisallowedEnchantments(@Nullable Identifier id, CallbackInfoReturnable<Optional<T>> cir) {
		if (Registry.class.cast(this) == ENCHANTMENT && !Enchancement.getConfig().allowedEnchantmentIdentifiers.isEmpty() && !Enchancement.getConfig().allowedEnchantmentIdentifiers.contains(id)) {
			cir.setReturnValue(Optional.of((T) ModEnchantments.EMPTY));
		}
	}
}
