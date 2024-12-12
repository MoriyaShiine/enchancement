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

public class AmethystShardEntity extends ShardEntity {
	private static final ParticleEffect PARTICLE = new ItemStackParticleEffect(ParticleTypes.ITEM, Items.AMETHYST_SHARD.getDefaultStack());

	public AmethystShardEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	public AmethystShardEntity(World world, LivingEntity owner, @Nullable ItemStack shotFrom) {
		super(ModEntityTypes.AMETHYST_SHARD, owner, world, shotFrom);
	}

	@Override
	protected ParticleEffect getParticleEffect() {
		return PARTICLE;
	}

	@Override
	protected RegistryKey<DamageType> getDamageType() {
		return ModDamageTypes.AMETHYST_SHARD;
	}

	@Override
	protected void onTargetHit(Entity entity) {
		if (isOnFire()) {
			entity.setOnFireFor(5);
		}
	}

	@Override
	public void setVelocity(double x, double y, double z, float power, float uncertainty) {
		super.setVelocity(x, y, z, power / 2, uncertainty);
	}
}
