/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.config;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v8.component.CardinalComponent;

public class SafeLightningComponent implements CardinalComponent {
	private boolean safe = false;

	@Override
	public void readData(ValueInput input) {
		safe = input.getBooleanOr("Safe", false);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("Safe", safe);
	}

	public boolean isSafe() {
		return safe;
	}

	public void setSafe(boolean safe) {
		this.safe = safe;
	}
}
