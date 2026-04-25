/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class IgniteKnockbackComponent implements ServerTickingComponent {
	private final LivingEntity obj;
	private boolean ignited = false;

	public IgniteKnockbackComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		ignited = input.getBooleanOr("Ignited", false);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("Ignited", ignited);
	}

	@Override
	public void serverTick() {
		if (ignited && !obj.isOnFire()) {
			ignited = false;
		}
	}

	public boolean isIgnited() {
		return ignited;
	}

	public void markIgnited() {
		ignited = true;
	}
}
