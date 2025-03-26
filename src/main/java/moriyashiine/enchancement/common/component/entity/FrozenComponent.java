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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
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
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		frozen = tag.getBoolean("Frozen", false);
		ticksFrozen = tag.getInt("TicksFrozen", 0);
		forcedPose = EntityPose.valueOf(tag.getString("ForcedPose", EntityPose.STANDING.toString()));
		forcedHeadYaw = tag.getFloat("ForcedHeadYaw", 0);
		forcedBodyYaw = tag.getFloat("ForcedBodyYaw", 0);
		forcedPitch = tag.getFloat("ForcedPitch", 0);
		forcedLimbSwingAnimationProgress = tag.getFloat("ForcedLimbSwingAnimationProgress", 0);
		forcedLimbSwingAmplitude = tag.getFloat("ForcedLimbAmplitude", 0);
		forcedClientAge = tag.getInt("ForcedClientAge", 0);
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("Frozen", frozen);
		tag.putInt("TicksFrozen", ticksFrozen);
		tag.putString("ForcedPose", forcedPose.toString());
		tag.putFloat("ForcedHeadYaw", forcedHeadYaw);
		tag.putFloat("ForcedBodyYaw", forcedBodyYaw);
		tag.putFloat("ForcedPitch", forcedPitch);
		tag.putFloat("ForcedLimbSwingAnimationProgress", forcedLimbSwingAnimationProgress);
		tag.putFloat("ForcedLimbAmplitude", forcedLimbSwingAmplitude);
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
					obj.damage((ServerWorld) obj.getWorld(), obj.getDamageSources().generic(), 2);
				}
				if (obj.horizontalCollision && obj.getVelocity().length() >= 0.05) {
					obj.damage((ServerWorld) obj.getWorld(), obj.getDamageSources().flyIntoWall(), 2);
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
		if (!obj.getWorld().isClient && !obj.getType().isIn(ModEntityTypeTags.CANNOT_FREEZE) && lastFreezingAttacker != null) {
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
