/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.ArrayList;
import java.util.List;

public class DisarmedWitchComponent implements Component {
	private final List<RegistryEntry<Potion>> disabledPotions = new ArrayList<>();

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		disabledPotions.clear();
		disabledPotions.addAll(tag.get("DisabledPotions", Potion.CODEC.listOf(), registryLookup.getOps(NbtOps.INSTANCE)).orElse(List.of()));
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.put("DisabledPotions", Potion.CODEC.listOf(), registryLookup.getOps(NbtOps.INSTANCE), List.copyOf(disabledPotions));
	}

	public boolean isDisabled(RegistryEntry<Potion> potion) {
		return disabledPotions.contains(potion);
	}

	public void disablePotion(RegistryEntry<Potion> potion) {
		disabledPotions.add(potion);
	}
}
