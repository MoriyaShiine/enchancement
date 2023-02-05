/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.entity.projectile;

import moriyashiine.enchancement.common.registry.ModEntityTypes;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class AmethystShardEntity extends PersistentProjectileEntity {
	private static final ParticleEffect PARTICLE = new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.AMETHYST_SHARD));

	public AmethystShardEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	public AmethystShardEntity(World world, LivingEntity owner) {
		super(ModEntityTypes.AMETHYST_SHARD, owner, world);
	}

	@Override
	protected ItemStack asItemStack() {
		return ItemStack.EMPTY;
	}

	@Override
	public void tick() {
		super.tick();
		if (!world.isClient && age > 400) {
			playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
			addParticles();
			discard();
		}
	}

	@Override
	protected SoundEvent getHitSound() {
		return ModSoundEvents.ENTITY_SHARD_SHATTER;
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		if (!world.isClient) {
			Entity entity = entityHitResult.getEntity();
			if (entity instanceof EnderDragonPart part) {
				entity = part.owner;
			}
			if (entity instanceof LivingEntity) {
				Entity owner = getOwner();
				if (EnchancementUtil.shouldHurt(owner, entity) && entity.damage(DamageSource.arrow(this, owner), (float) getDamage())) {
					if (isOnFire()) {
						entity.setOnFireFor(5);
					}
					entity.timeUntilRegen = 0;
					playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
					addParticles();
					discard();
				}
			}
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		BlockState state = world.getBlockState(blockHitResult.getBlockPos());
		state.onProjectileHit(world, state, blockHitResult, this);
		if (!world.isClient) {
			playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
			addParticles();
			discard();
		}
	}

	public void addParticles() {
		((ServerWorld) world).spawnParticles(PARTICLE, getX(), getY(), getZ(), 8, getWidth() / 2, getHeight() / 2, getWidth() / 2, 0);
	}
}
