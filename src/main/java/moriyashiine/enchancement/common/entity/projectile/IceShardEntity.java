/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.entity.projectile;

import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class IceShardEntity extends ShardEntity {
	private static final ParticleEffect PARTICLE = new ItemStackParticleEffect(ParticleTypes.ITEM, Items.ICE.getDefaultStack());

	public IceShardEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	public IceShardEntity(World world, LivingEntity owner, @Nullable ItemStack shotFrom) {
		super(ModEntityTypes.ICE_SHARD, owner, world, shotFrom);
	}

	public IceShardEntity(World world, LivingEntity source, @Nullable Entity owner) {
		this(world, source, (ItemStack) null);
		setOwner(owner);
	}

	@Override
	protected ParticleEffect getParticleEffect() {
		return PARTICLE;
	}

	@Override
	protected RegistryKey<DamageType> getDamageType() {
		return ModDamageTypes.ICE_SHARD;
	}

	@Override
	protected void onTargetHit(Entity entity) {
		if (entity.canFreeze()) {
			entity.setFrozenTicks(400);
		}
	}
}
