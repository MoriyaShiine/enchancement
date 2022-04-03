package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;

public class ChanellingComponent implements Component {
	private boolean disableFire = false;

	@Override
	public void readFromNbt(NbtCompound tag) {
		disableFire = tag.getBoolean("DisableFire");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("DisableFire", disableFire);
	}

	public boolean shouldDisableFire() {
		return disableFire;
	}

	public void setDisableFire(boolean disableFire) {
		this.disableFire = disableFire;
	}
}
