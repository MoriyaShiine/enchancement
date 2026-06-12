/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffecttype;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.EnchancementDamageTypes;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.init.EnchancementSoundEvents;
import moriyashiine.enchancement.common.tag.EnchancementEnchantmentTags;
import moriyashiine.enchancement.common.tag.EnchancementEntityTypeTags;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class FrozenComponent implements AutoSyncedComponent, CommonTickingComponent {
	private static final Identifier MOVEMENT_SPEED_ID = Enchancement.id("freeze_movement_speed");
	private static final int END_FREEZE_TICKS = 20;

	private final LivingEntity obj;
	private boolean frozen = false;
	private EntityReference<LivingEntity> lastFreezingAttacker = null;
	private int freezeTicks = 0;
	private Pose forcedPose = Pose.STANDING;
	private float forcedYHeadRot = 0, forcedVisualRotationYInDegrees = 0, forcedXRot = 0, forcedWalkAnimationPos = 0, forcedWalkAnimationSpeed = 0;
	private int forcedClientTickCount = 0;

	public FrozenComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		frozen = input.getBooleanOr("Frozen", false);
		lastFreezingAttacker = EntityReference.read(input, "LastFreezingAttacker");
		freezeTicks = input.getIntOr("FreezeTicks", 0);
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
		EntityReference.store(lastFreezingAttacker, output, "LastFreezingAttacker");
		output.putInt("FreezeTicks", freezeTicks);
		output.putString("ForcedPose", forcedPose.toString());
		output.putFloat("ForcedYHeadRot", forcedYHeadRot);
		output.putFloat("ForcedVisualRotationYInDegrees", forcedVisualRotationYInDegrees);
		output.putFloat("ForcedXRot", forcedXRot);
		output.putFloat("ForcedWalkAnimationPos", forcedWalkAnimationPos);
		output.putFloat("ForcedWalkAnimationSpeed", forcedWalkAnimationSpeed);
		output.putInt("ForcedClientTickCount", forcedClientTickCount);
	}

	@Override
	public void tick() {
		if (!frozen && freezeTicks > 0) {
			freezeTicks = Math.max(0, freezeTicks - (shouldThaw() ? 2 : 1));
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (frozen) {
			obj.setDeltaMovement(obj.getDeltaMovement().scale(0.98));
			if (!obj.isNoGravity()) {
				obj.setDeltaMovement(obj.getDeltaMovement().add(0, -0.02, 0));
			}
			if (freezeTicks == 0) {
				obj.setDeltaMovement(Vec3.ZERO);
			}
			if (shouldShatter()) {
				obj.hurtServer((ServerLevel) obj.level(), obj.damageSources().generic(), 2);
			}
			obj.move(MoverType.SELF, obj.getDeltaMovement());
			freezeTicks++;
		} else {
			if (freezeTicks == 0) {
				lastFreezingAttacker = null;
			} else if (freezeTicks >= END_FREEZE_TICKS && freezeTicks % 40 == 0 && !shouldThaw()) {
				obj.hurtServer((ServerLevel) obj.level(), obj.damageSources().freeze(), 1);
			}
		}
		AttributeInstance movementSpeed = obj.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			if (movementSpeed.hasModifier(MOVEMENT_SPEED_ID)) {
				movementSpeed.removeModifier(MOVEMENT_SPEED_ID);
			}
			if (!frozen) {
				float freezePercentage = getFreezePercentage();
				if (freezePercentage > 0) {
					SLibUtils.applyAttributeModifier(obj, Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_ID, -0.2 * freezePercentage, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), true);
				}
			}
		}
	}

	public void sync() {
		EnchancementEntityComponents.FROZEN.sync(obj);
	}

	public int getFreezeTicks() {
		return freezeTicks;
	}

	public void setFreezeTicks(int freezeTicks) {
		this.freezeTicks = freezeTicks;
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

	public @Nullable LivingEntity getLastFreezingAttacker() {
		return EntityReference.getLivingEntity(lastFreezingAttacker, obj.level());
	}

	public void setLastFreezingAttacker(@Nullable LivingEntity lastFreezingAttacker) {
		this.lastFreezingAttacker = EntityReference.of(lastFreezingAttacker);
	}

	public boolean isFreezing() {
		return freezeTicks >= END_FREEZE_TICKS;
	}

	public float getFreezePercentage() {
		return Math.min(1, freezeTicks / (float) END_FREEZE_TICKS);
	}

	public boolean shouldFreezeOnDeath(DamageSource source) {
		if (!obj.level().isClientSide() && !obj.is(EnchancementEntityTypeTags.CANNOT_FREEZE) && getLastFreezingAttacker() != null) {
			return source.is(DamageTypeTags.IS_FREEZING) || isSourceFreezeWeapon(source);
		}
		return false;
	}

	public void freeze() {
		obj.playSound(EnchancementSoundEvents.GENERIC_FREEZE, 1, 1);
		obj.setDeltaMovement(Vec3.ZERO);
		obj.setHealth(1);
		obj.setSilent(true);
		if (obj instanceof Mob mob) {
			mob.setNoAi(true);
		}
		frozen = true;
		freezeTicks = 0;
		forcedPose = obj.getPose();
		forcedYHeadRot = obj.getYHeadRot();
		forcedVisualRotationYInDegrees = obj.getVisualRotationYInDegrees();
		forcedXRot = obj.getXRot();
		forcedWalkAnimationPos = Mth.nextFloat(obj.getRandom(), -1, 1);
		forcedWalkAnimationSpeed = Mth.nextFloat(obj.getRandom(), -1, 1);
		forcedClientTickCount = obj.tickCount;
		sync();
	}

	private boolean shouldThaw() {
		return obj.isOnFire() || !obj.canFreeze();
	}

	private boolean shouldShatter() {
		if (obj.horizontalCollision && obj.getDeltaMovement().length() >= 0.05) {
			return true;
		}
		if (obj instanceof Mob mob && mob.isPersistenceRequired()) {
			return false;
		}
		return freezeTicks > 300 && obj.getRandom().nextFloat() < 1 / 64F;
	}

	public static boolean isSourceFreezeWeapon(DamageSource source) {
		if (source.is(EnchancementDamageTypes.ICE_SHARD)) {
			return true;
		}
		return source.getDirectEntity() instanceof LivingEntity living && EnchantmentHelper.hasTag(living.getMainHandItem(), EnchancementEnchantmentTags.FREEZES_ENTITIES);
	}
}
