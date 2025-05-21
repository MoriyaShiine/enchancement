/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class OwnedTridentComponent implements AutoSyncedComponent {
	private final TridentEntity obj;
	private boolean ownedByPlayer = false;
	private int slot = -1;

	public OwnedTridentComponent(TridentEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		ownedByPlayer = tag.getBoolean("OwnedByPlayer", false);
		slot = tag.getInt("Slot", -1);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("OwnedByPlayer", ownedByPlayer);
		tag.putInt("Slot", slot);
	}

	public void sync() {
		ModEntityComponents.OWNED_TRIDENT.get(obj);
	}

	public boolean isOwnedByPlayer() {
		return ownedByPlayer;
	}

	public int getSlot() {
		return slot;
	}

	public void markPlayerOwned(int slot) {
		this.ownedByPlayer = true;
		this.slot = slot;
	}
}
