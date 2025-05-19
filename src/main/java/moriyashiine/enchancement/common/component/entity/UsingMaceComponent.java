/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class UsingMaceComponent implements AutoSyncedComponent {
	private boolean using = false;

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		using = tag.getBoolean("Using", false);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("Using", using);
	}

	public void setUsing(boolean using) {
		this.using = using;
	}

	public boolean isUsing() {
		return using;
	}
}
