/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.HashSet;
import java.util.Set;

public class DisarmedWitchComponent implements Component {
	private final Set<Potion> disabledPotions = new HashSet<>();

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		NbtList disabledPotions = tag.getList("DisabledPotions", NbtElement.STRING_TYPE);
		for (int i = 0; i < disabledPotions.size(); i++) {
			this.disabledPotions.add(Registries.POTION.get(Identifier.tryParse(disabledPotions.getString(i))));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		NbtList disabledPotions = new NbtList();
		this.disabledPotions.forEach(potion -> disabledPotions.add(NbtString.of(Registries.POTION.getId(potion).toString())));
		tag.put("DisabledPotions", disabledPotions);
	}

	public boolean isDisabled(RegistryEntry<Potion> potion) {
		return disabledPotions.contains(potion.value());
	}

	public void disablePotion(Potion potion) {
		disabledPotions.add(potion);
	}
}
