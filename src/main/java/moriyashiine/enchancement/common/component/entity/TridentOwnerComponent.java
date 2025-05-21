/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class TridentOwnerComponent implements AutoSyncedComponent {
	private final TridentEntity obj;
	private boolean ownedByPlayer = false;

	public TridentOwnerComponent(TridentEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		ownedByPlayer = tag.getBoolean("OwnedByPlayer", false);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("OwnedByPlayer", ownedByPlayer);
	}

	public void sync() {
		ModEntityComponents.TRIDENT_OWNER.get(obj);
	}

	public boolean isOwnedByPlayer() {
		return ownedByPlayer;
	}

	public void setOwnedByPlayer(boolean ownedByPlayer) {
		this.ownedByPlayer = ownedByPlayer;
	}
}
