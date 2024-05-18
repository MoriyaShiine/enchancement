/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;

public class ChannelingComponent implements Component {
	private boolean safe = false;

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		safe = tag.getBoolean("Safe");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("Safe", safe);
	}

	public boolean isSafe() {
		return safe;
	}

	public void setSafe(boolean safe) {
		this.safe = safe;
	}
}
