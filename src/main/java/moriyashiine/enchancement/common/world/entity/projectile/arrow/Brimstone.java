/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.entity.projectile.arrow;

import moriyashiine.enchancement.client.payload.PlayBrimstoneTravelSoundPayload;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import moriyashiine.enchancement.common.init.ModParticleTypes;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Brimstone extends AbstractArrow {
	public static final ItemStack BRIMSTONE_STACK;
	public static final int DISTANCE_PER_TICK = 6;

	static {
		BRIMSTONE_STACK = new ItemStack(Items.LAVA_BUCKET);
		BRIMSTONE_STACK.set(ModComponentTypes.BRIMSTONE_DAMAGE, Integer.MAX_VALUE);
	}

	public static int getMaxTicks() {
		return Mth.floor(256F / DISTANCE_PER_TICK);
	}

	public static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(Brimstone.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> FORCED_X_ROT = SynchedEntityData.defineId(Brimstone.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> FORCED_Y_ROT = SynchedEntityData.defineId(Brimstone.class, EntityDataSerializers.FLOAT);

	private static final DustParticleOptions PARTICLE = new DustParticleOptions(0xFF0000, 1);

	public int distanceTraveled = 0, ticksExisted = 0;

	private final Set<Entity> hitEntities = new HashSet<>(), killedEntities = new HashSet<>();

	public Brimstone(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
	}

	public Brimstone(Level level, LivingEntity mob, @Nullable ItemStack firedFromWeapon) {
		super(ModEntityTypes.BRIMSTONE, mob, level, ItemStack.EMPTY, firedFromWeapon);
		setPos(mob.getX(), mob.getEyeY() - 0.3, mob.getZ());
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public void tick() {
		if (!level().isClientSide() && ticksExisted == 0) {
			PlayerLookup.all(level().getServer()).forEach(receiver -> PlayBrimstoneTravelSoundPayload.send(receiver, this));
		}
		if (isCritArrow()) {
			setCritArrow(false);
		}
		setDeltaMovement(Vec3.ZERO);
		for (int i = 0; i < DISTANCE_PER_TICK; i++) {
			float min = Math.min(distanceTraveled, ticksExisted);
			if (min > 0 && min == distanceTraveled) {
				discard();
			}
			Vec3 start = position().add(getLookAngle().scale(distanceTraveled + (ticksExisted > 0 ? -1 : 0))), end = start.add(getLookAngle());
			BlockHitResult hitResult = level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
			if (hitResult.getType() == HitResult.Type.BLOCK) {
				if (level().isClientSide()) {
					addParticles(PARTICLE, hitResult.getLocation().x(), hitResult.getLocation().y(), hitResult.getLocation().z());
				} else {
					level().gameEvent(GameEvent.PROJECTILE_LAND, hitResult.getLocation(), GameEvent.Context.of(this));
				}
				break;
			} else {
				Entity owner = getOwner();
				level().getEntities(owner, AABB.unitCubeFromLowerCorner(hitResult.getLocation()).inflate(0.5), EntitySelector.NO_SPECTATORS.and(entity -> canEntityBeHit(owner, entity))).forEach(entity -> {
					if (level() instanceof ServerLevel world) {
						double damage = getDamage();
						if (entity instanceof LivingEntity living) {
							damage *= living.getMaxHealth() / 20F;
						}
						damage *= getDamageMultiplier(distanceTraveled);
						damage = Math.min(50, damage);
						entity.hurtServer(world, world.damageSources().source(ModDamageTypes.BRIMSTONE, this, owner), (float) damage);
						hitEntities.add(entity);
						if (entity instanceof LivingEntity living && living.isDeadOrDying()) {
							killedEntities.add(living);
						}
					} else {
						addParticles(PARTICLE, entity.getX(), entity.getRandomY(), entity.getZ());
					}
				});
			}
			distanceTraveled++;
		}
		if (level().isClientSide()) {
			Vec3 particlePos = position().add(getLookAngle().scale(distanceTraveled));
			addParticles(PARTICLE, particlePos.x(), particlePos.y(), particlePos.z());
			addParticles(ModParticleTypes.BRIMSTONE_BUBBLE, particlePos.x(), particlePos.y(), particlePos.z());
		} else if (ticksExisted > getMaxTicks()) {
			discard();
		}
		ticksExisted++;
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
	}

	@Override
	protected void onHitBlock(BlockHitResult hitResult) {
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		setBaseDamage(input.getFloatOr("Damage", 0));
		entityData.set(FORCED_X_ROT, input.getFloatOr("ForcedXRot", 0));
		entityData.set(FORCED_Y_ROT, input.getFloatOr("ForcedYRot", 0));
		distanceTraveled = input.getIntOr("DistanceTraveled", 0);
		ticksExisted = input.getIntOr("TicksExisted", 0);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putFloat("Damage", getDamage());
		output.putFloat("ForcedXRot", getXRot());
		output.putFloat("ForcedYRot", getYRot());
		output.putInt("DistanceTraveled", distanceTraveled);
		output.putInt("TicksExisted", ticksExisted);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder entityData) {
		super.defineSynchedData(entityData);
		entityData.define(DAMAGE, 0F);
		entityData.define(FORCED_X_ROT, 0F);
		entityData.define(FORCED_Y_ROT, 0F);
	}

	@Override
	public float getXRot() {
		return entityData.get(FORCED_X_ROT);
	}

	@Override
	public float getYRot() {
		return entityData.get(FORCED_Y_ROT);
	}

	@Override
	public void setBaseDamage(double damage) {
		entityData.set(DAMAGE, (float) damage);
	}

	public float getDamage() {
		return entityData.get(DAMAGE);
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		if (reason.shouldDestroy() && getOwner() instanceof ServerPlayer player) {
			CriteriaTriggers.KILLED_BY_ARROW.trigger(player, killedEntities, getWeaponItem());
		}
	}

	public float getDamageMultiplier(int distanceTraveled) {
		final float threshold = 12;
		if (distanceTraveled < threshold) {
			return Mth.lerp(distanceTraveled / threshold, 0.25F, 1);
		}
		return Math.min(2, Mth.lerp((distanceTraveled - threshold) / 200F, 1F, 2F));
	}

	private void addParticles(ParticleOptions particle, double x, double y, double z) {
		float range = Mth.lerp(getDamage() / 12, 0, 0.5F);
		for (int i = 0; i < 8; i++) {
			level().addAlwaysVisibleParticle(particle,
					true,
					x + Mth.nextFloat(random, -range, range),
					y + Mth.nextFloat(random, -range, range),
					z + Mth.nextFloat(random, -range, range),
					Mth.nextFloat(random, -1, 1),
					Mth.nextFloat(random, -1, 1),
					Mth.nextFloat(random, -1, 1));
		}
	}

	private boolean canEntityBeHit(Entity owner, Entity entity) {
		if (entity instanceof LivingEntity || entity.is(ModEntityTypeTags.BRIMSTONE_HITTABLE)) {
			return !hitEntities.contains(entity) && entity.isAlive() && SLibUtils.shouldHurt(owner, entity);
		}
		return false;
	}
}
