package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class JumpingComponent implements AutoSyncedComponent {
	private final PlayerEntity obj;
	private boolean jumping = false;

	public JumpingComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		jumping = tag.getBoolean("Jumping");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("Jumping", jumping);
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public void sync() {
		ModEntityComponents.JUMPING.sync(obj);
	}
}
