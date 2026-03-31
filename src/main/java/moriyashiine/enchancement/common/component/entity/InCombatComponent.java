/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class InCombatComponent implements ServerTickingComponent {
	private int combatTicks = 0;

	@Override
	public void readData(ValueInput input) {
		combatTicks = input.getIntOr("CombatTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putInt("CombatTicks", combatTicks);
	}

	@Override
	public void serverTick() {
		if (combatTicks > 0) {
			combatTicks--;
		}
	}

	public boolean inCombat() {
		return combatTicks > 0;
	}

	public void setInCombat() {
		combatTicks = 320;
	}
}
