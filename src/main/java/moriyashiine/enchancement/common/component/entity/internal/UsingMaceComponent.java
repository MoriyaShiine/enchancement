/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.internal;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class UsingMaceComponent implements AutoSyncedComponent {
	private boolean using = false;

	@Override
	public void readData(ValueInput input) {
		using = input.getBooleanOr("Using", false);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("Using", using);
	}

	public boolean isUsing() {
		return using;
	}

	public void setUsing(boolean using) {
		this.using = using;
	}
}
