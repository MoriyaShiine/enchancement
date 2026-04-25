/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffecttype;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class FrozenSquidComponent implements AutoSyncedComponent {
	private final Squid obj;
	private float forcedTentacleAngle = 0, forcedXBodyRot = 0, forcedZBodyRot = 0;

	public FrozenSquidComponent(Squid obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		forcedTentacleAngle = input.getFloatOr("ForceTentacleAngle", 0);
		forcedXBodyRot = input.getFloatOr("ForcedXBodyRot", 0);
		forcedZBodyRot = input.getFloatOr("ForcedZBodyRot", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putFloat("ForceTentacleAngle", forcedTentacleAngle);
		output.putFloat("ForcedXBodyRot", forcedXBodyRot);
		output.putFloat("ForcedZBodyRot", forcedZBodyRot);
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

	public float getForcedXBodyRot() {
		return forcedXBodyRot;
	}

	public void setForcedXBodyRot(float forcedXBodyRot) {
		this.forcedXBodyRot = forcedXBodyRot;
	}

	public float getForcedZBodyRot() {
		return forcedZBodyRot;
	}

	public void setForcedZBodyRot(float forcedZBodyRot) {
		this.forcedZBodyRot = forcedZBodyRot;
	}
}
