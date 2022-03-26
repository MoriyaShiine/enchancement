package moriyashiine.enchancement.common.entity.projectile;

import moriyashiine.enchancement.client.packet.AddIceShardParticlesPacket;
import moriyashiine.enchancement.common.registry.ModEntityTypes;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class IceShardEntity extends PersistentProjectileEntity {
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
			if (entity == getOwner() || (entity instanceof PlayerEntity hitPlayer && getOwner() instanceof PlayerEntity ownerPlayer && !ownerPlayer.shouldDamagePlayer(hitPlayer))) {
				return;
			}
			entity.damage(new ProjectileDamageSource("freeze", this, getOwner()), 4);
			entity.setFrozenTicks(400);
			playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
			addIceShardParticles();
			discard();
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

	private void addIceShardParticles() {
		PlayerLookup.tracking(this).forEach(foundPlayer -> AddIceShardParticlesPacket.send(foundPlayer, this));
	}
}
