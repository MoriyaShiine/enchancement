/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.entity.projectile;

import moriyashiine.enchancement.common.registry.ModEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TorchEntity extends PersistentProjectileEntity {
	public boolean shouldPlaceTorch = true;

	public TorchEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	public TorchEntity(World world, LivingEntity owner) {
		super(ModEntityTypes.TORCH, owner, world);
		shouldPlaceTorch = owner.isSneaking();
	}

	@Override
	protected ItemStack asItemStack() {
		return new ItemStack(Items.TORCH);
	}

	@Override
	public void tick() {
		super.tick();
		if (getWorld().isClient) {
			getWorld().addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(), 0, 0, 0);
			getWorld().addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0, 0);
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		int stuckArrows = 0;
		Entity entity = entityHitResult.getEntity();
		if (entity instanceof EnderDragonPart part) {
			entity = part.owner;
		}
		if (entity instanceof LivingEntity living) {
			living.setOnFireFor(2);
			playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1, 1);
			if (!getWorld().isClient) {
				stuckArrows = living.getStuckArrowCount();
			}
		}
		super.onEntityHit(entityHitResult);
		if (!getWorld().isClient && entityHitResult.getEntity() instanceof LivingEntity living && living.getStuckArrowCount() != stuckArrows) {
			living.setStuckArrowCount(stuckArrows);
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		BlockState state = getWorld().getBlockState(blockHitResult.getBlockPos());
		state.onProjectileHit(getWorld(), state, blockHitResult, this);
		if (!getWorld().isClient) {
			discard();
			if (shouldPlaceTorch && getOwner() instanceof PlayerEntity player) {
				BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
				ItemPlacementContext context = new ItemPlacementContext(player, Hand.MAIN_HAND, asItemStack(), blockHitResult);
				if (context.canPlace()) {
					state = Blocks.TORCH.getPlacementState(context);
					if (state != null && state.canPlaceAt(getWorld(), pos)) {
						getWorld().setBlockState(pos, state);
						playSound(Blocks.TORCH.getDefaultState().getSoundGroup().getPlaceSound(), 1, 1);
						return;
					} else {
						state = Blocks.WALL_TORCH.getPlacementState(context);
						if (state != null && state.canPlaceAt(getWorld(), pos)) {
							getWorld().setBlockState(pos, state);
							playSound(Blocks.TORCH.getDefaultState().getSoundGroup().getPlaceSound(), 1, 1);
							return;
						}
					}
				}
			}
			dropStack(asItemStack(), 0.1F);
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		shouldPlaceTorch = nbt.getBoolean("ShouldPlaceTorch");
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("ShouldPlaceTorch", shouldPlaceTorch);
	}
}
