/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class PhasingComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final ArrowEntity obj;
	private boolean shouldPhase = false;
	private int ticksInAir = 0;

	public PhasingComponent(ArrowEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		shouldPhase = tag.getBoolean("ShouldPhase");
		ticksInAir = tag.getInt("TicksInAir");
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		tag.putBoolean("ShouldPhase", shouldPhase);
		tag.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void serverTick() {
		if (shouldPhase && ticksInAir++ >= 200) {
			shouldPhase = false;
			obj.setNoGravity(false);
		}
	}

	public void sync() {
		ModEntityComponents.PHASHING.sync(obj);
	}

	public void setShouldPhase(boolean shouldPhase) {
		this.shouldPhase = shouldPhase;
	}

	public boolean shouldPhase() {
		return shouldPhase;
	}
}
