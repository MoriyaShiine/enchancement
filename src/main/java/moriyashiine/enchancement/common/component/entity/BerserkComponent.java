/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class BerserkComponent implements ServerTickingComponent {
	private int preventRegenerationTicks = 0;

	@Override
	public void readFromNbt(NbtCompound tag) {
		preventRegenerationTicks = tag.getInt("PreventRegenerationTicks");
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		tag.putInt("PreventRegenerationTicks", preventRegenerationTicks);
	}

	@Override
	public void serverTick() {
		if (preventRegenerationTicks > 0) {
			preventRegenerationTicks--;
		}
	}

	public int getPreventRegenerationTicks() {
		return preventRegenerationTicks;
	}

	public void setPreventRegenerationTicks(int preventRegenerationTicks) {
		this.preventRegenerationTicks = preventRegenerationTicks;
	}
}
