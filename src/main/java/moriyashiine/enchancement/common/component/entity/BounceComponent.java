/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class BounceComponent implements ServerTickingComponent, AutoSyncedComponent {
	private final LivingEntity obj;
	private boolean invertedBounce = false, justBounced = false;
	private int ticksOnGround = 0;

	public BounceComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		invertedBounce = input.getBooleanOr("InvertedBounce", false);
		justBounced = input.getBooleanOr("JustBounced", false);
		ticksOnGround = input.getIntOr("TicksOnGround", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("InvertedBounce", invertedBounce);
		output.putBoolean("JustBounced", justBounced);
		output.putInt("TicksOnGround", ticksOnGround);
	}

	@Override
	public void serverTick() {
		if (ticksOnGround == 5) {
			justBounced = false;
		}
		if (ticksOnGround < 5 && obj.onGround()) {
			ticksOnGround++;
		} else {
			ticksOnGround = 0;
		}
	}

	public boolean hasInvertedBounce() {
		return invertedBounce;
	}

	public void setInvertedBounce(boolean invertedBounce) {
		this.invertedBounce = invertedBounce;
	}

	public boolean justBounced() {
		return justBounced;
	}

	public void markBounced() {
		justBounced = true;
	}
}
