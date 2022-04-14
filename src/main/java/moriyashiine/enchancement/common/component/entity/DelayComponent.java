package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class DelayComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final ArrowEntity obj;
	private ItemStack stackShotFrom = null;
	private Vec3d storedVelocity = null;
	private boolean hasDelay = false;
	private float forcedPitch = 0, forcedYaw = 0;
	private int ticksFloating = 0;

	public DelayComponent(ArrowEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		if (tag.contains("StoredVelocity")) {
			NbtCompound storedVelocity = tag.getCompound("StoredVelocity");
			this.storedVelocity = new Vec3d(storedVelocity.getDouble("X"), storedVelocity.getDouble("Y"), storedVelocity.getDouble("Z"));
			hasDelay = tag.getBoolean("HasDelay");
			ticksFloating = tag.getInt("TicksFloating");
			forcedPitch = tag.getFloat("ForcedPitch");
			forcedYaw = tag.getFloat("ForcedYaw");
		}
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if (storedVelocity != null) {
			NbtCompound storedVelocity = new NbtCompound();
			storedVelocity.putDouble("X", this.storedVelocity.getX());
			storedVelocity.putDouble("Y", this.storedVelocity.getY());
			storedVelocity.putDouble("Z", this.storedVelocity.getZ());
			tag.put("StoredVelocity", storedVelocity);
			tag.putBoolean("HasDelay", hasDelay);
			tag.putInt("TicksFloating", ticksFloating);
			tag.putFloat("ForcedPitch", forcedPitch);
			tag.putFloat("ForcedYaw", forcedYaw);
		}
	}

	@Override
	public void tick() {
		if (hasDelay) {
			if (!obj.world.isClient) {
				if (storedVelocity == null) {
					storedVelocity = obj.getVelocity();
					forcedPitch = obj.getPitch();
					forcedYaw = obj.getYaw();
					ModEntityComponents.DELAY.sync(obj);
				}
				if (ticksFloating > 300 || (obj.getOwner() instanceof LivingEntity living && living.handSwinging && (living.getMainHandStack() == stackShotFrom || living.getOffHandStack() == stackShotFrom))) {
					obj.setDamage(obj.getDamage() * MathHelper.lerp(Math.min(1, ticksFloating / 100F), 1, 2.5));
					obj.setVelocity(storedVelocity);
					storedVelocity = null;
					setHasDelay(false);
					sync();
					return;
				}
			}
			ticksFloating++;
			obj.setVelocity(Vec3d.ZERO);
			obj.setPitch(forcedPitch);
			obj.setYaw(forcedYaw);
		}
	}

	public void sync() {
		ModEntityComponents.DELAY.sync(obj);
	}

	public void setStackShotFrom(ItemStack stackShotFrom) {
		this.stackShotFrom = stackShotFrom;
	}

	public void setHasDelay(boolean hasDelay) {
		this.hasDelay = hasDelay;
	}

	public boolean shouldChangeParticles() {
		return ticksFloating >= 100;
	}
}
