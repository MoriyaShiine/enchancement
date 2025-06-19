/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
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
	public void readData(ReadView readView) {
		invertedBounce = readView.getBoolean("InvertedBounce", false);
		justBounced = readView.getBoolean("JustBounced", false);
		ticksOnGround = readView.getInt("TicksOnGround", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.putBoolean("InvertedBounce", invertedBounce);
		writeView.putBoolean("JustBounced", justBounced);
		writeView.putInt("TicksOnGround", ticksOnGround);
	}

	@Override
	public void serverTick() {
		if (ticksOnGround == 5) {
			justBounced = false;
		}
		if (ticksOnGround < 5 && obj.isOnGround()) {
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
