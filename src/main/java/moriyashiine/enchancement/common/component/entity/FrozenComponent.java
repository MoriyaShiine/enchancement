package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.registry.ModComponents;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;

public class FrozenComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final MobEntity obj;
	private boolean frozen = false;
	private int ticksFrozen = 0;
	private float forcedHeadYaw, forcedBodyYaw, forcedPitch, forcedLimbDistance, forcedLimbAngle;

	public FrozenComponent(MobEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		frozen = tag.getBoolean("Frozen");
		ticksFrozen = tag.getInt("TicksFrozen");
		forcedHeadYaw = tag.getFloat("ForcedHeadYaw");
		forcedBodyYaw = tag.getFloat("ForcedBodyYaw");
		forcedPitch = tag.getFloat("ForcedPitch");
		forcedLimbDistance = tag.getFloat("ForceLimbDistance");
		forcedLimbAngle = tag.getFloat("ForcedLimbAngle");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("Frozen", frozen);
		tag.putInt("TicksFrozen", ticksFrozen);
		tag.putFloat("ForcedHeadYaw", forcedHeadYaw);
		tag.putFloat("ForcedBodyYaw", forcedBodyYaw);
		tag.putFloat("ForcedPitch", forcedPitch);
		tag.putFloat("ForceLimbDistance", forcedLimbDistance);
		tag.putFloat("ForcedLimbAngle", forcedLimbAngle);
	}

	@Override
	public void tick() {
		if (!obj.world.isClient) {
			if (isFrozen()) {
				if (!obj.isAiDisabled()) {
					obj.setAiDisabled(true);
				}
				setTicksFrozen(getTicksFrozen() + 1);
				if (getTicksFrozen() > 200 && obj.getRandom().nextFloat() < 1 / 64F && !obj.hasCustomName()) {
					obj.damage(DamageSource.GENERIC, 2);
				}
				if (obj.horizontalCollision && obj.getVelocity().length() >= 0.05) {
					obj.damage(DamageSource.FLY_INTO_WALL, 2);
				}
				if (getTicksFrozen() <= 10) {
					obj.setVelocity(obj.getVelocity().multiply(0.25));
				}
				if (!obj.hasNoGravity()) {
					obj.setVelocity(obj.getVelocity().add(0, -0.02, 0));
				}
				obj.move(MovementType.SELF, obj.getVelocity());
			}
		}
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
		ModComponents.FROZEN.sync(obj);
	}

	public int getTicksFrozen() {
		return ticksFrozen;
	}

	public void setTicksFrozen(int ticksFrozen) {
		this.ticksFrozen = ticksFrozen;
	}

	public float getForcedHeadYaw() {
		return forcedHeadYaw;
	}

	public void setForcedHeadYaw(float forcedHeadYaw) {
		this.forcedHeadYaw = forcedHeadYaw;
	}

	public float getForcedBodyYaw() {
		return forcedBodyYaw;
	}

	public void setForcedBodyYaw(float forcedBodyYaw) {
		this.forcedBodyYaw = forcedBodyYaw;
	}

	public float getForcedPitch() {
		return forcedPitch;
	}

	public void setForcedPitch(float forcedPitch) {
		this.forcedPitch = forcedPitch;
	}

	public float getForcedLimbDistance() {
		return forcedLimbDistance;
	}

	public void setForcedLimbDistance(float forcedLimbDistance) {
		this.forcedLimbDistance = forcedLimbDistance;
	}

	public float getForcedLimbAngle() {
		return forcedLimbAngle;
	}

	public void setForcedLimbAngle(float forcedLimbAngle) {
		this.forcedLimbAngle = forcedLimbAngle;
	}

	public void freeze() {
		setForcedHeadYaw(obj.headYaw);
		setForcedBodyYaw(obj.bodyYaw);
		setForcedPitch(obj.getPitch());
		setForcedLimbDistance(obj.limbDistance);
		setForcedLimbAngle(obj.limbAngle);
		setFrozen(true);
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_FREEZE, 1, 1);
		obj.setSilent(true);
		obj.setHealth(1);
	}
}
