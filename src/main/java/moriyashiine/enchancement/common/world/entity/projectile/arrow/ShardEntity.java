/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.entity.projectile.arrow;

import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jspecify.annotations.Nullable;

public abstract class ShardEntity extends AbstractArrow {
	protected ShardEntity(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
	}

	protected ShardEntity(EntityType<? extends AbstractArrow> type, LivingEntity mob, Level level, @Nullable ItemStack firedFromWeapon) {
		super(type, mob, level, ItemStack.EMPTY, firedFromWeapon);
	}

	protected abstract ParticleOptions getParticleEffect();

	protected abstract ResourceKey<DamageType> getDamageType();

	protected void onTargetHit(Entity entity) {
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return ItemStack.EMPTY;
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return ModSoundEvents.SHARD_SHATTER;
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide() && tickCount > 400) {
			playSound(getDefaultHitGroundSoundEvent(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
			addParticles();
			discard();
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		if (level() instanceof ServerLevel world) {
			Entity entity = entityHitResult.getEntity();
			if (entity instanceof EnderDragonPart part) {
				entity = part.parentMob;
			}
			Entity owner = getOwner();
			if (SLibUtils.shouldHurt(owner, entity) && entity.hurtServer(world, world.damageSources().source(getDamageType(), this, owner), (float) baseDamage)) {
				onTargetHit(entity);
				playSound(getDefaultHitGroundSoundEvent(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
				addParticles();
				discard();
			}
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		BlockState state = level().getBlockState(blockHitResult.getBlockPos());
		state.onProjectileHit(level(), state, blockHitResult, this);
		if (!level().isClientSide()) {
			playSound(getDefaultHitGroundSoundEvent(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
			addParticles();
			discard();
		}
	}

	public void addParticles() {
		((ServerLevel) level()).sendParticles(getParticleEffect(), getX(), getY(), getZ(), 8, getBbWidth() / 2, getBbHeight() / 2, getBbWidth() / 2, 0);
	}
}
