/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffecttype;

import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class FrozenComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity obj;
	private Entity lastFreezingAttacker = null;
	private boolean frozen = false;
	private int ticksFrozen = 0;
	private Pose forcedPose = Pose.STANDING;
	private float forcedYHeadRot = 0, forcedVisualRotationYInDegrees = 0, forcedXRot = 0, forcedWalkAnimationPos = 0, forcedWalkAnimationSpeed = 0;
	private int forcedClientTickCount = 0;

	public FrozenComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		frozen = input.getBooleanOr("Frozen", false);
		ticksFrozen = input.getIntOr("TicksFrozen", 0);
		forcedPose = Pose.valueOf(input.getStringOr("ForcedPose", Pose.STANDING.toString()));
		forcedYHeadRot = input.getFloatOr("ForcedYHeadRot", 0);
		forcedVisualRotationYInDegrees = input.getFloatOr("ForcedVisualRotationYInDegrees", 0);
		forcedXRot = input.getFloatOr("ForcedXRot", 0);
		forcedWalkAnimationPos = input.getFloatOr("ForcedWalkAnimationPos", 0);
		forcedWalkAnimationSpeed = input.getFloatOr("ForcedWalkAnimationSpeed", 0);
		forcedClientTickCount = input.getIntOr("ForcedClientTickCount", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("Frozen", frozen);
		output.putInt("TicksFrozen", ticksFrozen);
		output.putString("ForcedPose", forcedPose.toString());
		output.putFloat("ForcedYHeadRot", forcedYHeadRot);
		output.putFloat("ForcedVisualRotationYInDegrees", forcedVisualRotationYInDegrees);
		output.putFloat("ForcedXRot", forcedXRot);
		output.putFloat("ForcedWalkAnimationPos", forcedWalkAnimationPos);
		output.putFloat("ForcedWalkAnimationSpeed", forcedWalkAnimationSpeed);
		output.putInt("ForcedClientTickCount", forcedClientTickCount);
	}

	@Override
	public void serverTick() {
		if (frozen) {
			if (obj instanceof Mob mob) {
				if (!mob.isNoAi()) {
					mob.setNoAi(true);
				}
				ticksFrozen++;
				if (ticksFrozen > 200 && obj.getRandom().nextFloat() < 1 / 64F && !mob.isPersistenceRequired()) {
					obj.hurtServer((ServerLevel) obj.level(), obj.damageSources().generic(), 2);
				}
				if (obj.horizontalCollision && obj.getDeltaMovement().length() >= 0.05) {
					obj.hurtServer((ServerLevel) obj.level(), obj.damageSources().flyIntoWall(), 2);
				}
				if (ticksFrozen <= 10) {
					obj.setDeltaMovement(obj.getDeltaMovement().scale(0.25));
				}
				if (!obj.isNoGravity()) {
					obj.setDeltaMovement(obj.getDeltaMovement().add(0, -0.02, 0));
				}
				obj.move(MoverType.SELF, obj.getDeltaMovement());
			}
		} else if (lastFreezingAttacker != null && obj.getTicksFrozen() <= 0) {
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

	public Pose getForcedPose() {
		return forcedPose;
	}

	public float getForcedYHeadRot() {
		return forcedYHeadRot;
	}

	public float getForcedVisualRotationYInDegrees() {
		return forcedVisualRotationYInDegrees;
	}

	public float getForcedXRot() {
		return forcedXRot;
	}

	public float getForcedWalkAnimationPos() {
		return forcedWalkAnimationPos;
	}

	public float getForcedWalkAnimationSpeed() {
		return forcedWalkAnimationSpeed;
	}

	public int getForcedClientTickCount() {
		return forcedClientTickCount;
	}

	public boolean shouldFreezeOnDeath(DamageSource source) {
		if (!obj.level().isClientSide() && !obj.is(ModEntityTypeTags.CANNOT_FREEZE) && lastFreezingAttacker != null) {
			return source.is(DamageTypeTags.IS_FREEZING) || isSourceFrostbiteWeapon(source);
		}
		return false;
	}

	public void freeze() {
		obj.playSound(ModSoundEvents.GENERIC_FREEZE, 1, 1);
		obj.setSilent(true);
		obj.setHealth(1);
		forcedPose = obj.getPose();
		forcedYHeadRot = obj.getYHeadRot();
		forcedVisualRotationYInDegrees = obj.getVisualRotationYInDegrees();
		forcedXRot = obj.getXRot();
		forcedWalkAnimationPos = Mth.nextFloat(obj.getRandom(), -1, 1);
		forcedWalkAnimationSpeed = Mth.nextFloat(obj.getRandom(), -1, 1);
		forcedClientTickCount = obj.tickCount;
		frozen = true;
		sync();
	}

	public static boolean isSourceFrostbiteWeapon(DamageSource source) {
		return source.is(ModDamageTypes.ICE_SHARD) || (source.getDirectEntity() instanceof LivingEntity living && EnchantmentHelper.hasTag(living.getMainHandItem(), ModEnchantmentTags.FREEZES_ENTITIES));
	}
}
