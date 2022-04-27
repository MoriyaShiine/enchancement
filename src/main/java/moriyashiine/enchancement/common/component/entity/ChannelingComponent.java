package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;

public class ChannelingComponent implements Component {
	private boolean safe = false;

	@Override
	public void readFromNbt(NbtCompound tag) {
		safe = tag.getBoolean("Safe");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("Safe", safe);
	}

	public boolean isSafe() {
		return safe;
	}

	public void setSafe(boolean safe) {
		this.safe = safe;
	}
}
