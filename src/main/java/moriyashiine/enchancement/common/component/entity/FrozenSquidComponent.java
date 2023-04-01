/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.nbt.NbtCompound;

public class FrozenSquidComponent implements AutoSyncedComponent {
	private final SquidEntity obj;
	private float forcedRollAngle = 0, forcedTentacleAngle = 0, forcedTiltAngle = 0;

	public FrozenSquidComponent(SquidEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		forcedRollAngle = tag.getFloat("ForcedRollAngle");
		forcedTentacleAngle = tag.getFloat("ForceTentacleAngle");
		forcedTiltAngle = tag.getFloat("ForcedTiltAngle");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putFloat("ForcedRollAngle", forcedRollAngle);
		tag.putFloat("ForceTentacleAngle", forcedTentacleAngle);
		tag.putFloat("ForcedTiltAngle", forcedTiltAngle);
	}

	public void sync() {
		ModEntityComponents.FROZEN_SQUID.sync(obj);
	}

	public float getForcedRollAngle() {
		return forcedRollAngle;
	}

	public void setForcedRollAngle(float forcedRollAngle) {
		this.forcedRollAngle = forcedRollAngle;
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
}
