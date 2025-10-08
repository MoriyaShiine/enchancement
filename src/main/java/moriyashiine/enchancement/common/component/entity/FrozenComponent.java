/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class FrozenComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity obj;
	private Entity lastFreezingAttacker = null;
	private boolean frozen = false;
	private int ticksFrozen = 0;
	private EntityPose forcedPose = EntityPose.STANDING;
	private float forcedHeadYaw = 0, forcedBodyYaw = 0, forcedPitch = 0, forcedLimbSwingAnimationProgress = 0, forcedLimbSwingAmplitude = 0;
	private int forcedClientAge = 0;

	public FrozenComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		frozen = readView.getBoolean("Frozen", false);
		ticksFrozen = readView.getInt("TicksFrozen", 0);
		forcedPose = EntityPose.valueOf(readView.getString("ForcedPose", EntityPose.STANDING.toString()));
		forcedHeadYaw = readView.getFloat("ForcedHeadYaw", 0);
		forcedBodyYaw = readView.getFloat("ForcedBodyYaw", 0);
		forcedPitch = readView.getFloat("ForcedPitch", 0);
		forcedLimbSwingAnimationProgress = readView.getFloat("ForcedLimbSwingAnimationProgress", 0);
		forcedLimbSwingAmplitude = readView.getFloat("ForcedLimbAmplitude", 0);
		forcedClientAge = readView.getInt("ForcedClientAge", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.putBoolean("Frozen", frozen);
		writeView.putInt("TicksFrozen", ticksFrozen);
		writeView.putString("ForcedPose", forcedPose.toString());
		writeView.putFloat("ForcedHeadYaw", forcedHeadYaw);
		writeView.putFloat("ForcedBodyYaw", forcedBodyYaw);
		writeView.putFloat("ForcedPitch", forcedPitch);
		writeView.putFloat("ForcedLimbSwingAnimationProgress", forcedLimbSwingAnimationProgress);
		writeView.putFloat("ForcedLimbAmplitude", forcedLimbSwingAmplitude);
		writeView.putInt("ForcedClientAge", forcedClientAge);
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
					obj.damage((ServerWorld) obj.getEntityWorld(), obj.getDamageSources().generic(), 2);
				}
				if (obj.horizontalCollision && obj.getVelocity().length() >= 0.05) {
					obj.damage((ServerWorld) obj.getEntityWorld(), obj.getDamageSources().flyIntoWall(), 2);
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

	public float getForcedLimbSwingAnimationProgress() {
		return forcedLimbSwingAnimationProgress;
	}

	public float getForcedLimbSwingAmplitude() {
		return forcedLimbSwingAmplitude;
	}

	public int getForcedClientAge() {
		return forcedClientAge;
	}

	public boolean shouldFreezeOnDeath(DamageSource source) {
		if (!obj.getEntityWorld().isClient() && !obj.getType().isIn(ModEntityTypeTags.CANNOT_FREEZE) && lastFreezingAttacker != null) {
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
		forcedLimbSwingAnimationProgress = MathHelper.nextFloat(obj.getRandom(), -1, 1);
		forcedLimbSwingAmplitude = MathHelper.nextFloat(obj.getRandom(), -1, 1);
		forcedClientAge = obj.age;
		frozen = true;
		sync();
	}

	public static boolean isSourceFrostbiteWeapon(DamageSource source) {
		return source.isOf(ModDamageTypes.ICE_SHARD) || (source.getSource() instanceof LivingEntity living && EnchantmentHelper.hasAnyEnchantmentsIn(living.getMainHandStack(), ModEnchantmentTags.FREEZES_ENTITIES));
	}
}
