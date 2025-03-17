/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import com.mojang.serialization.Lifecycle;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.concurrent.CompletableFuture;

public class ModDynamicRegistryProvider extends FabricDynamicRegistryProvider {
	public ModDynamicRegistryProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
		ModEnchantments.bootstrap(createRegisterable(registries, entries));
	}

	@Override
	public String getName() {
		return Enchancement.MOD_ID + "_dynamic";
	}

	private static <T> Registerable<T> createRegisterable(RegistryWrapper.WrapperLookup registries, Entries entries) {
		return new Registerable<>() {
			@Override
			public RegistryEntry.Reference<T> register(RegistryKey<T> key, T value, Lifecycle lifecycle) {
				return (RegistryEntry.Reference<T>) entries.add(key, value);
			}

			@Override
			public <S> RegistryEntryLookup<S> getRegistryLookup(RegistryKey<? extends Registry<? extends S>> registryRef) {
				return registries.getOrThrow(registryRef);
			}
		};
	}
}
