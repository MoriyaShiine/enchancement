/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.ArrayList;
import java.util.List;

public class DisarmedWitchComponent implements Component {
	private final List<RegistryEntry<Potion>> disabledPotions = new ArrayList<>();

	@Override
	public void readData(ReadView readView) {
		disabledPotions.clear();
		disabledPotions.addAll(readView.read("DisabledPotions", Potion.CODEC.listOf()).orElse(List.of()));
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.put("DisabledPotions", Potion.CODEC.listOf(), disabledPotions);
	}

	public boolean isDisabled(RegistryEntry<Potion> potion) {
		return disabledPotions.contains(potion);
	}

	public void disablePotion(RegistryEntry<Potion> potion) {
		disabledPotions.add(potion);
	}
}
