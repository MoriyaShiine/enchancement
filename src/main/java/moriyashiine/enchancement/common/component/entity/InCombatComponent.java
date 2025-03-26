/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class InCombatComponent implements ServerTickingComponent {
	private int combatTicks = 0;

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		combatTicks = tag.getInt("CombatTicks", 0);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putInt("CombatTicks", combatTicks);
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
