/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.mojang.serialization.Lifecycle;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.RemovedRegistryEntry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unchecked")
@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> extends Registry<T> {
	protected SimpleRegistryMixin(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle) {
		super(key, lifecycle);
	}

	@Inject(method = "set(ILnet/minecraft/util/registry/RegistryKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;Z)Lnet/minecraft/util/registry/RegistryEntry;", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(int rawId, RegistryKey<T> key, T value, Lifecycle lifecycle, boolean checkDuplicateKeys, CallbackInfoReturnable<RegistryEntry<T>> cir) {
		if (SimpleRegistry.class.cast(this) == Registry.ENCHANTMENT && !ModConfig.isEnchantmentAllowed(key.getValue())) {
			RemovedRegistryEntry.REMOVED_ENTRIES.add(new RemovedRegistryEntry((Enchantment) value, key.getValue(), rawId));
			cir.setReturnValue(RegistryEntry.Reference.standAlone(this, key));
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
	private void enchancement$disableDisallowedEnchantments(T value, CallbackInfoReturnable<@Nullable Identifier> cir) {
		if (SimpleRegistry.class.cast(this) == Registry.ENCHANTMENT) {
			RemovedRegistryEntry removedEntry = RemovedRegistryEntry.getFromEnchantment((Enchantment) value);
			if (removedEntry != null) {
				cir.setReturnValue(removedEntry.identifier());
			}
		}
	}

	@Inject(method = "getRawId", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantmentsRawid(@Nullable T value, CallbackInfoReturnable<Integer> cir) {
		if (value != null && SimpleRegistry.class.cast(this) == Registry.ENCHANTMENT) {
			RemovedRegistryEntry removedEntry = RemovedRegistryEntry.getFromEnchantment((Enchantment) value);
			if (removedEntry != null) {
				cir.setReturnValue(removedEntry.rawId());
			}
		}
	}
}
