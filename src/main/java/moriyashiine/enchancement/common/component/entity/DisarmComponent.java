/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.Component;

public class DisarmComponent implements Component {
	private boolean hasDisarm = false;

	@Override
	public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
	}

	public boolean hasDisarm() {
		return hasDisarm;
	}

	public void setHasDisarm(boolean hasDisarm) {
		this.hasDisarm = hasDisarm;
	}
}
