/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.mojang.serialization.Lifecycle;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.RemovedRegistryEntry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryOwner;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unchecked")
@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> {
	@Shadow
	public abstract RegistryEntryOwner<T> getEntryOwner();

	@Inject(method = "set(ILnet/minecraft/registry/RegistryKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;)Lnet/minecraft/registry/entry/RegistryEntry$Reference;", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(int i, RegistryKey<T> registryKey, T object, Lifecycle lifecycle, CallbackInfoReturnable<RegistryEntry.Reference<T>> cir) {
		if (SimpleRegistry.class.cast(this) == Registries.ENCHANTMENT && !ModConfig.isEnchantmentAllowed(registryKey.getValue())) {
			RemovedRegistryEntry.REMOVED_ENTRIES.add(new RemovedRegistryEntry((Enchantment) object, registryKey.getValue(), i));
			cir.setReturnValue(RegistryEntry.Reference.standAlone(getEntryOwner(), registryKey));
		}
	}

	@Inject(method = "get(Lnet/minecraft/registry/RegistryKey;)Ljava/lang/Object;", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(@Nullable RegistryKey<T> key, CallbackInfoReturnable<@Nullable T> cir) {
		if (key != null && SimpleRegistry.class.cast(this) == Registries.ENCHANTMENT) {
			RemovedRegistryEntry removedEntry = RemovedRegistryEntry.getFromId(key.getValue());
			if (removedEntry != null) {
				cir.setReturnValue((T) removedEntry.enchantment());
			}
		}
	}

	@Inject(method = "get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(@Nullable Identifier id, CallbackInfoReturnable<@Nullable T> cir) {
		if (id != null && SimpleRegistry.class.cast(this) == Registries.ENCHANTMENT) {
			RemovedRegistryEntry removedEntry = RemovedRegistryEntry.getFromId(id);
			if (removedEntry != null) {
				cir.setReturnValue((T) removedEntry.enchantment());
			}
		}
	}

	@Inject(method = "getId", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(T value, CallbackInfoReturnable<@Nullable Identifier> cir) {
		if (SimpleRegistry.class.cast(this) == Registries.ENCHANTMENT) {
			RemovedRegistryEntry removedEntry = RemovedRegistryEntry.getFromEnchantment((Enchantment) value);
			if (removedEntry != null) {
				cir.setReturnValue(removedEntry.identifier());
			}
		}
	}

	@Inject(method = "getRawId", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantmentsRawid(@Nullable T value, CallbackInfoReturnable<Integer> cir) {
		if (value != null && SimpleRegistry.class.cast(this) == Registries.ENCHANTMENT) {
			RemovedRegistryEntry removedEntry = RemovedRegistryEntry.getFromEnchantment((Enchantment) value);
			if (removedEntry != null) {
				cir.setReturnValue(removedEntry.rawId());
			}
		}
	}
}
