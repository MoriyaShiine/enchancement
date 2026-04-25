/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.config;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class OwnedTridentComponent implements AutoSyncedComponent {
	private final ThrownTrident obj;
	private boolean ownedByPlayer = false;
	private int slot = -1;

	public OwnedTridentComponent(ThrownTrident obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		ownedByPlayer = input.getBooleanOr("OwnedByPlayer", false);
		slot = input.getIntOr("Slot", -1);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("OwnedByPlayer", ownedByPlayer);
		output.putInt("Slot", slot);
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
