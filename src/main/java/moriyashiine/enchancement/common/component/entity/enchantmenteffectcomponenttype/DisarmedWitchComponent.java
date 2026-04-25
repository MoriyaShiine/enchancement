/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v8.component.CardinalComponent;

import java.util.ArrayList;
import java.util.List;

public class DisarmedWitchComponent implements CardinalComponent {
	private final List<Holder<Potion>> disabledPotions = new ArrayList<>();

	@Override
	public void readData(ValueInput input) {
		disabledPotions.clear();
		disabledPotions.addAll(input.read("DisabledPotions", Potion.CODEC.listOf()).orElse(List.of()));
	}

	@Override
	public void writeData(ValueOutput output) {
		output.store("DisabledPotions", Potion.CODEC.listOf(), disabledPotions);
	}

	public boolean isDisabled(Holder<Potion> potion) {
		return disabledPotions.contains(potion);
	}

	public void disablePotion(Holder<Potion> potion) {
		disabledPotions.add(potion);
	}
}
