/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class BounceComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private boolean invertedBounce = false;
	private double nextBounceStrength = 0;

	public BounceComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		invertedBounce = input.getBooleanOr("InvertedBounce", false);
		nextBounceStrength = input.getDoubleOr("NextBounceStrength", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("InvertedBounce", invertedBounce);
		output.putDouble("NextBounceStrength", nextBounceStrength);
	}

	@Override
	public void tick() {
		if (nextBounceStrength != 0) {
			if (obj.canSimulateMovement()) {
				obj.setDeltaMovement(obj.getDeltaMovement().x(), nextBounceStrength, obj.getDeltaMovement().z());
			}
			nextBounceStrength = 0;
		}
	}

	public boolean hasInvertedBounce() {
		return invertedBounce;
	}

	public void setInvertedBounce(boolean invertedBounce) {
		this.invertedBounce = invertedBounce;
	}

	public void bounce(double bounceStrength) {
		nextBounceStrength = bounceStrength;
	}
}
