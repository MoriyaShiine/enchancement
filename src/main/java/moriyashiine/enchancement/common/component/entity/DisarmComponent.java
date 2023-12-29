/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class DisarmComponent implements Component {
	private boolean hasDisarm = false;

	@Override
	public void readFromNbt(@NotNull NbtCompound tag) {
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
	}

	public boolean hasDisarm() {
		return hasDisarm;
	}

	public void setHasDisarm(boolean hasDisarm) {
		this.hasDisarm = hasDisarm;
	}
}
