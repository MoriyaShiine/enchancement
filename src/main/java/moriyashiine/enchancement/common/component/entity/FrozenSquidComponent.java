/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class FrozenSquidComponent implements AutoSyncedComponent {
	private final SquidEntity obj;
	private float forcedTentacleAngle = 0, forcedTiltAngle = 0, forcedRollAngle = 0;

	public FrozenSquidComponent(SquidEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		forcedTentacleAngle = tag.getFloat("ForceTentacleAngle");
		forcedTiltAngle = tag.getFloat("ForcedTiltAngle");
		forcedRollAngle = tag.getFloat("ForcedRollAngle");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putFloat("ForceTentacleAngle", forcedTentacleAngle);
		tag.putFloat("ForcedTiltAngle", forcedTiltAngle);
		tag.putFloat("ForcedRollAngle", forcedRollAngle);
	}

	public void sync() {
		ModEntityComponents.FROZEN_SQUID.sync(obj);
	}

	public float getForcedTentacleAngle() {
		return forcedTentacleAngle;
	}

	public void setForcedTentacleAngle(float forcedTentacleAngle) {
		this.forcedTentacleAngle = forcedTentacleAngle;
	}

	public float getForcedTiltAngle() {
		return forcedTiltAngle;
	}

	public void setForcedTiltAngle(float forcedTiltAngle) {
		this.forcedTiltAngle = forcedTiltAngle;
	}

	public float getForcedRollAngle() {
		return forcedRollAngle;
	}

	public void setForcedRollAngle(float forcedRollAngle) {
		this.forcedRollAngle = forcedRollAngle;
	}
}
