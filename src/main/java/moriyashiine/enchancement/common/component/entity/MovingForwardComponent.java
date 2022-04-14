package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class MovingForwardComponent implements AutoSyncedComponent {
	private final PlayerEntity obj;
	private boolean movingForward = false;

	public MovingForwardComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		movingForward = tag.getBoolean("MovingForward");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("MovingForward", movingForward);
	}

	public void sync() {
		ModEntityComponents.MOVING_FORWARD.sync(obj);
	}

	public boolean isMovingForward() {
		return movingForward;
	}

	public void setMovingForward(boolean movingForward) {
		this.movingForward = movingForward;
	}
}
