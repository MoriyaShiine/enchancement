package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class BuryComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity obj;
	private BlockPos buryPos = null;

	public BuryComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		if (tag.contains("BuryPos")) {
			buryPos = BlockPos.fromLong(tag.getLong("BuryPos"));
		} else {
			buryPos = null;
		}
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if (buryPos != null) {
			tag.putLong("BuryPos", buryPos.asLong());
		}
	}

	@Override
	public void serverTick() {
		if (buryPos != null) {
			if (obj.getX() != buryPos.getX() + 0.5 || obj.getY() != buryPos.getY() + 0.5 || obj.getZ() != buryPos.getZ() + 0.5) {
				obj.teleport(buryPos.getX() + 0.5, buryPos.getY() + 0.5, buryPos.getZ() + 0.5);
			}
			if (obj.getVelocity() != Vec3d.ZERO) {
				obj.setVelocity(Vec3d.ZERO);
				obj.velocityModified = true;
			}
		}
	}

	public BlockPos getBuryPos() {
		return buryPos;
	}

	public void setBuryPos(BlockPos buryPos) {
		this.buryPos = buryPos;
	}

	public void unbury() {
		setBuryPos(null);
		sync();
	}

	public void sync() {
		ModEntityComponents.BURY.sync(obj);
	}
}
