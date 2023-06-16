/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.enchancement.common.registry.*;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class FrozenComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity obj;
	private Entity lastFreezingAttacker = null;
	private boolean frozen = false;
	private int ticksFrozen = 0;
	private EntityPose forcedPose = EntityPose.STANDING;
	private float forcedHeadYaw = 0, forcedBodyYaw = 0, forcedPitch = 0, forcedLimbAngle = 0, forcedLimbDistance = 0;
	private int forcedClientAge = 0;

	public FrozenComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		frozen = tag.getBoolean("Frozen");
		ticksFrozen = tag.getInt("TicksFrozen");
		forcedPose = EntityPose.valueOf(tag.getString("ForcedPose"));
		forcedHeadYaw = tag.getFloat("ForcedHeadYaw");
		forcedBodyYaw = tag.getFloat("ForcedBodyYaw");
		forcedPitch = tag.getFloat("ForcedPitch");
		forcedLimbAngle = tag.getFloat("ForcedLimbAngle");
		forcedLimbDistance = tag.getFloat("ForcedLimbDistance");
		forcedClientAge = tag.getInt("ForcedClientAge");
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		tag.putBoolean("Frozen", frozen);
		tag.putInt("TicksFrozen", ticksFrozen);
		tag.putString("ForcedPose", forcedPose.toString());
		tag.putFloat("ForcedHeadYaw", forcedHeadYaw);
		tag.putFloat("ForcedBodyYaw", forcedBodyYaw);
		tag.putFloat("ForcedPitch", forcedPitch);
		tag.putFloat("ForcedLimbAngle", forcedLimbAngle);
		tag.putFloat("ForcedLimbDistance", forcedLimbDistance);
		tag.putInt("ForcedClientAge", forcedClientAge);
	}

	@Override
	public void serverTick() {
		if (frozen) {
			if (obj instanceof MobEntity mob) {
				if (!mob.isAiDisabled()) {
					mob.setAiDisabled(true);
				}
				ticksFrozen++;
				if (ticksFrozen > 200 && obj.getRandom().nextFloat() < 1 / 64F && !mob.isPersistent()) {
					obj.damage(obj.getDamageSources().generic(), 2);
				}
				if (obj.horizontalCollision && obj.getVelocity().length() >= 0.05) {
					obj.damage(obj.getDamageSources().flyIntoWall(), 2);
				}
				if (ticksFrozen <= 10) {
					obj.setVelocity(obj.getVelocity().multiply(0.25));
				}
				if (!obj.hasNoGravity()) {
					obj.setVelocity(obj.getVelocity().add(0, -0.02, 0));
				}
				obj.move(MovementType.SELF, obj.getVelocity());
			}
		} else if (lastFreezingAttacker != null && obj.getFrozenTicks() <= 0) {
			lastFreezingAttacker = null;
		}
	}

	public void sync() {
		ModEntityComponents.FROZEN.sync(obj);
	}

	public Entity getLastFreezingAttacker() {
		return lastFreezingAttacker;
	}

	public void setLastFreezingAttacker(Entity lastFreezingAttacker) {
		this.lastFreezingAttacker = lastFreezingAttacker;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public EntityPose getForcedPose() {
		return forcedPose;
	}

	public float getForcedHeadYaw() {
		return forcedHeadYaw;
	}

	public float getForcedBodyYaw() {
		return forcedBodyYaw;
	}

	public float getForcedPitch() {
		return forcedPitch;
	}

	public float getForcedLimbAngle() {
		return forcedLimbDistance;
	}

	public float getForcedLimbDistance() {
		return forcedLimbAngle;
	}

	public int getForcedClientAge() {
		return forcedClientAge;
	}

	public boolean shouldFreezeOnDeath(DamageSource source) {
		if (!obj.getWorld().isClient && !obj.getType().isIn(ModTags.EntityTypes.CANNOT_FREEZE) && lastFreezingAttacker != null) {
			return source.isIn(DamageTypeTags.IS_FREEZING) || isSourceFrostbiteWeapon(source);
		}
		return false;
	}

	public void freeze() {
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_FREEZE, 1, 1);
		obj.setSilent(true);
		obj.setHealth(1);
		forcedPose = obj.getPose();
		forcedHeadYaw = obj.getHeadYaw();
		forcedBodyYaw = obj.getBodyYaw();
		forcedPitch = obj.getPitch();
		forcedLimbAngle = MathHelper.nextFloat(obj.getRandom(), -1, 1);
		forcedLimbDistance = MathHelper.nextFloat(obj.getRandom(), -1, 1);
		forcedClientAge = obj.age;
		frozen = true;
		sync();
	}

	public static boolean isSourceFrostbiteWeapon(DamageSource source) {
		return source.isOf(ModDamageTypes.ICE_SHARD) || (source.getSource() instanceof LivingEntity living && EnchancementUtil.hasEnchantment(ModEnchantments.FROSTBITE, living));
	}
}
