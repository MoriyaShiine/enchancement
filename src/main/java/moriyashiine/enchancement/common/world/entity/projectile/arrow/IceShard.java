/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.entity.projectile.arrow;

import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class IceShard extends ShardEntity {
	private static final ParticleOptions PARTICLE = new ItemParticleOption(ParticleTypes.ITEM, Items.ICE);

	public IceShard(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
	}

	public IceShard(Level level, LivingEntity mob, @Nullable ItemStack firedFromWeapon) {
		super(ModEntityTypes.ICE_SHARD, mob, level, firedFromWeapon);
	}

	public IceShard(Level level, LivingEntity source, @Nullable Entity owner) {
		this(level, source, (ItemStack) null);
		setOwner(owner);
	}

	@Override
	protected ParticleOptions getParticleEffect() {
		return PARTICLE;
	}

	@Override
	protected ResourceKey<DamageType> getDamageType() {
		return ModDamageTypes.ICE_SHARD;
	}

	@Override
	protected void onTargetHit(Entity entity) {
		if (entity.canFreeze()) {
			entity.setTicksFrozen(400);
		}
	}
}
