/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class DisarmedWitchComponent implements Component {
	private final Set<Potion> disabledPotions = new HashSet<>();

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList disabledPotions = tag.getList("DisabledPotions", NbtElement.STRING_TYPE);
		for (int i = 0; i < disabledPotions.size(); i++) {
			this.disabledPotions.add(Registries.POTION.get(Identifier.tryParse(disabledPotions.getString(i))));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList disabledPotions = new NbtList();
		this.disabledPotions.forEach(potion -> disabledPotions.add(NbtString.of(Registries.POTION.getId(potion).toString())));
		tag.put("DisabledPotions", disabledPotions);
	}

	public boolean isDisabled(Potion potion) {
		return disabledPotions.contains(potion);
	}

	public void disablePotion(Potion potion) {
		disabledPotions.add(potion);
	}
}
