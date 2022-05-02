package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.mojang.serialization.Lifecycle;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.RemovedRegistryEntry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"ConstantConditions", "unchecked"})
@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> {
	@SuppressWarnings("ConstantConditions")
	@Inject(method = "set(ILnet/minecraft/util/registry/RegistryKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;Z)Ljava/lang/Object;", at = @At("HEAD"), cancellable = true)
	private <V extends T> void enchancement$disableDisallowedEnchantments(int rawId, RegistryKey<T> key, V entry, Lifecycle lifecycle, boolean checkDuplicateKeys, CallbackInfoReturnable<V> cir) {
		if (SimpleRegistry.class.cast(this) == Registry.ENCHANTMENT && Enchancement.getConfig().isEnchantmentDisallowed(key.getValue())) {
			RemovedRegistryEntry.REMOVED_ENTRIES.add(new RemovedRegistryEntry((Enchantment) entry, key.getValue(), rawId));
			cir.setReturnValue(entry);
		}
	}

	@Inject(method = "get(Lnet/minecraft/util/registry/RegistryKey;)Ljava/lang/Object;", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(@Nullable RegistryKey<T> key, CallbackInfoReturnable<@Nullable T> cir) {
		if (key != null && SimpleRegistry.class.cast(this) == Registry.ENCHANTMENT) {
			RemovedRegistryEntry removedEntry = RemovedRegistryEntry.getFromId(key.getValue());
			if (removedEntry != null) {
				cir.setReturnValue((T) removedEntry.enchantment());
			}
		}
	}

	@Inject(method = "get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(@Nullable Identifier id, CallbackInfoReturnable<@Nullable T> cir) {
		if (id != null && SimpleRegistry.class.cast(this) == Registry.ENCHANTMENT) {
			RemovedRegistryEntry removedEntry = RemovedRegistryEntry.getFromId(id);
			if (removedEntry != null) {
				cir.setReturnValue((T) removedEntry.enchantment());
			}
		}
	}

	@Inject(method = "getId", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(T entry, CallbackInfoReturnable<@Nullable Identifier> cir) {
		if (SimpleRegistry.class.cast(this) == Registry.ENCHANTMENT) {
			RemovedRegistryEntry removedEntry = RemovedRegistryEntry.getFromEnchantment((Enchantment) entry);
			if (removedEntry != null) {
				cir.setReturnValue(removedEntry.identifier());
			}
		}
	}

	@Inject(method = "getRawId", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantmentsRawid(@Nullable T entry, CallbackInfoReturnable<Integer> cir) {
		if (entry != null && SimpleRegistry.class.cast(this) == Registry.ENCHANTMENT) {
			RemovedRegistryEntry removedEntry = RemovedRegistryEntry.getFromEnchantment((Enchantment) entry);
			if (removedEntry != null) {
				cir.setReturnValue(removedEntry.rawId());
			}
		}
	}
}
