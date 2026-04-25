/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v8.component.CardinalComponent;

public class DisarmedWanderingTraderComponent implements CardinalComponent {
	private boolean disarmedPotion = false, disarmedMilk = false;

	@Override
	public void readData(ValueInput input) {
		disarmedMilk = input.getBooleanOr("DisarmedMilk", false);
		disarmedPotion = input.getBooleanOr("DisarmedPotion", false);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("DisarmedMilk", disarmedMilk);
		output.putBoolean("DisarmedPotion", disarmedPotion);
	}

	public boolean disarmedMilk() {
		return disarmedMilk;
	}

	public void disarmMilk() {
		this.disarmedMilk = true;
	}

	public boolean disarmedPotion() {
		return disarmedPotion;
	}

	public void disarmPotion() {
		this.disarmedPotion = true;
	}
}
