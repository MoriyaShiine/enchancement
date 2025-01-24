/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class BounceComponent implements AutoSyncedComponent {
	private boolean invertedBounce = false;

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		invertedBounce = tag.getBoolean("InvertedBounce");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("InvertedBounce", invertedBounce);
	}

	public boolean hasInvertedBounce() {
		return invertedBounce;
	}

	public void setInvertedBounce(boolean invertedBounce) {
		this.invertedBounce = invertedBounce;
	}
}
