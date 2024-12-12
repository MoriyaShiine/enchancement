/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class FrozenGuardianComponent implements AutoSyncedComponent {
	private final GuardianEntity obj;
	private float forcedTailAngle = 0, forcedSpikesExtension = 0;

	public FrozenGuardianComponent(GuardianEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		forcedTailAngle = tag.getFloat("ForcedTailAngle");
		forcedSpikesExtension = tag.getFloat("ForcedSpikesExtension");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putFloat("ForcedTailAngle", forcedTailAngle);
		tag.putFloat("ForcedSpikesExtension", forcedSpikesExtension);
	}

	public void sync() {
		ModEntityComponents.FROZEN_GUARDIAN.sync(obj);
	}

	public float getForcedTailAngle() {
		return forcedTailAngle;
	}

	public void setForcedTailAngle(float forcedTailAngle) {
		this.forcedTailAngle = forcedTailAngle;
	}

	public float getForcedSpikesExtension() {
		return forcedSpikesExtension;
	}

	public void setForcedSpikesExtension(float forcedSpikesExtension) {
		this.forcedSpikesExtension = forcedSpikesExtension;
	}
}
