/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class FrozenGuardianComponent implements AutoSyncedComponent {
	private final GuardianEntity obj;
	private float forcedTailAngle = 0, forcedSpikesExtension = 0;

	public FrozenGuardianComponent(GuardianEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		forcedTailAngle = readView.getFloat("ForcedTailAngle", 0);
		forcedSpikesExtension = readView.getFloat("ForcedSpikesExtension", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.putFloat("ForcedTailAngle", forcedTailAngle);
		writeView.putFloat("ForcedSpikesExtension", forcedSpikesExtension);
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
