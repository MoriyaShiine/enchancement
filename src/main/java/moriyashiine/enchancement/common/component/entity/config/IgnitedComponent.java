/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.config;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class IgnitedComponent implements ServerTickingComponent {
	private final LivingEntity obj;
	private boolean ignited = false, ignoreFireResistance = false;

	public IgnitedComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		ignited = input.getBooleanOr("Ignited", false);
		ignoreFireResistance = input.getBooleanOr("IgnoreFireResistance", false);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("Ignited", ignited);
		output.putBoolean("IgnoreFireResistance", ignoreFireResistance);
	}

	@Override
	public void serverTick() {
		if (ignited && !obj.isOnFire()) {
			ignited = ignoreFireResistance = false;
		}
	}

	public boolean isIgnited() {
		return ignited;
	}

	public void markIgnited() {
		ignited = true;
	}

	public boolean ignoreFireResistance() {
		return ignoreFireResistance;
	}

	public void alternateIgnoreFireResistance() {
		ignoreFireResistance = !ignoreFireResistance;
	}
}
