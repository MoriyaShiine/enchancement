package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.registry.ModComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class BuryComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private BlockPos buryPos = null;

	public BuryComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		if (tag.contains("BuryPos")) {
			buryPos = BlockPos.fromLong(tag.getLong("BuryPos"));
		}
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if (buryPos != null) {
			tag.putLong("BuryPos", buryPos.asLong());
		}
	}

	@Override
	public void tick() {
		if (buryPos != null) {
			obj.teleport(buryPos.getX() + 0.5, buryPos.getY() + 0.5, buryPos.getZ() + 0.5);
			obj.setVelocity(Vec3d.ZERO);
			obj.velocityModified = true;
		}
	}

	public BlockPos getBuryPos() {
		return buryPos;
	}

	public void setBuryPos(BlockPos buryPos) {
		this.buryPos = buryPos;
		ModComponents.BURY.sync(obj);
	}
}
