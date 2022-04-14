package moriyashiine.enchancement.common.entity.projectile;

import moriyashiine.enchancement.common.component.entity.FrozenComponent;
import moriyashiine.enchancement.common.registry.ModEntityTypes;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.ProjectileDamageSource;
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

public class IceShardEntity extends PersistentProjectileEntity {
	private static final ParticleEffect PARTICLE = new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.ICE));

	public IceShardEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	public IceShardEntity(World world, LivingEntity owner) {
		super(ModEntityTypes.ICE_SHARD, owner, world);
	}

	@Override
	protected ItemStack asItemStack() {
		return ItemStack.EMPTY;
	}

	@Override
	protected SoundEvent getHitSound() {
		return ModSoundEvents.ENTITY_ICE_SHARD_SHATTER;
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		if (!world.isClient) {
			Entity entity = entityHitResult.getEntity();
			Entity owner = getOwner();
			if (FrozenComponent.shouldHurt(owner, entity) && entity.damage(new ProjectileDamageSource("freeze", this, owner), 4)) {
				entity.setFrozenTicks(400);
				playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
				addIceShardParticles();
				discard();
			}
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		if (!world.isClient) {
			addIceShardParticles();
			discard();
		}
	}

	public void addIceShardParticles() {
		((ServerWorld) world).spawnParticles(PARTICLE, getX(), getY(), getZ(), 8, getWidth() / 2, getHeight() / 2, getWidth() / 2, 0);
	}
}
