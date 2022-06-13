/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;

public class MovingForwardComponent implements AutoSyncedComponent {
	private boolean movingForward = false;

	@Override
	public void readFromNbt(NbtCompound tag) {
		movingForward = tag.getBoolean("MovingForward");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("MovingForward", movingForward);
	}

	public boolean isMovingForward() {
		return movingForward;
	}

	public void setMovingForward(boolean movingForward) {
		this.movingForward = movingForward;
	}
}
