/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.entity.projectile;

import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class ShardEntity extends PersistentProjectileEntity {
	protected ShardEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	protected ShardEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, @Nullable ItemStack shotFrom) {
		super(type, owner, world, ItemStack.EMPTY, shotFrom);
	}

	protected abstract ParticleEffect getParticleEffect();

	protected abstract RegistryKey<DamageType> getDamageType();

	protected void onTargetHit(Entity entity) {
	}

	@Override
	protected ItemStack getDefaultItemStack() {
		return ItemStack.EMPTY;
	}

	@Override
	protected SoundEvent getHitSound() {
		return ModSoundEvents.ENTITY_SHARD_SHATTER;
	}

	@Override
	public void tick() {
		super.tick();
		if (!getEntityWorld().isClient() && age > 400) {
			playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
			addParticles();
			discard();
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		if (getEntityWorld() instanceof ServerWorld world) {
			Entity entity = entityHitResult.getEntity();
			if (entity instanceof EnderDragonPart part) {
				entity = part.owner;
			}
			Entity owner = getOwner();
			if (SLibUtils.shouldHurt(owner, entity) && entity.damage(world, world.getDamageSources().create(getDamageType(), this, owner), (float) damage)) {
				onTargetHit(entity);
				playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
				addParticles();
				discard();
			}
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		BlockState state = getEntityWorld().getBlockState(blockHitResult.getBlockPos());
		state.onProjectileHit(getEntityWorld(), state, blockHitResult, this);
		if (!getEntityWorld().isClient()) {
			playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
			addParticles();
			discard();
		}
	}

	public void addParticles() {
		((ServerWorld) getEntityWorld()).spawnParticles(getParticleEffect(), getX(), getY(), getZ(), 8, getWidth() / 2, getHeight() / 2, getWidth() / 2, 0);
	}
}
