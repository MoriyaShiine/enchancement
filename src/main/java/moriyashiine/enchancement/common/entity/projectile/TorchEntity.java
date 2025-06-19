/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.entity.projectile;

import moriyashiine.enchancement.common.init.ModEntityTypes;
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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TorchEntity extends PersistentProjectileEntity {
	private boolean canFunction = true, shouldPlaceTorch = true;
	private int ignitionTime = 0;

	public TorchEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	public TorchEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
		super(ModEntityTypes.TORCH, owner, world, stack, shotFrom);
		if (pickupType != PickupPermission.ALLOWED && !(owner instanceof PlayerEntity player && player.isCreative())) {
			canFunction = false;
		}
		shouldPlaceTorch = owner.isSneaking();
	}

	@Override
	protected ItemStack getDefaultItemStack() {
		return Items.TORCH.getDefaultStack();
	}

	@Override
	public void tick() {
		super.tick();
		if (getWorld().isClient) {
			getWorld().addParticleClient(ParticleTypes.FLAME, getX(), getY(), getZ(), 0, 0, 0);
			getWorld().addParticleClient(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0, 0);
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		Entity entity = entityHitResult.getEntity();
		if (entity instanceof EnderDragonPart part) {
			entity = part.owner;
		}
		if (entity instanceof LivingEntity living && entity.getType() != EntityType.ENDERMAN) {
			playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1, 1);
			if (!getWorld().isClient) {
				living.setOnFireFor(Math.min(16, MathHelper.ceil(living.getFireTicks() / 20F) + ignitionTime));
			}
		}
		super.onEntityHit(entityHitResult);
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		BlockState state = getWorld().getBlockState(blockHitResult.getBlockPos());
		state.onProjectileHit(getWorld(), state, blockHitResult, this);
		if (getWorld() instanceof ServerWorld serverWorld) {
			discard();
			if (canFunction) {
				if (shouldPlaceTorch && getOwner() instanceof PlayerEntity player && player.getAbilities().allowModifyWorld) {
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
				if (getOwner() instanceof PlayerEntity player && !player.isCreative()) {
					dropStack(serverWorld, asItemStack(), 0.1F);
				}
			}
		}
	}

	@Override
	protected void readCustomData(ReadView view) {
		super.readCustomData(view);
		canFunction = view.getBoolean("CanFunction", false);
		shouldPlaceTorch = view.getBoolean("ShouldPlaceTorch", false);
		ignitionTime = view.getInt("IgnitionTime", 0);
	}

	@Override
	protected void writeCustomData(WriteView view) {
		super.writeCustomData(view);
		view.putBoolean("CanFunction", canFunction);
		view.putBoolean("ShouldPlaceTorch", shouldPlaceTorch);
		view.putInt("IgnitionTime", ignitionTime);
	}

	@Override
	public boolean isOnFire() {
		return true;
	}

	@Override
	public boolean doesRenderOnFire() {
		return false;
	}

	public void setIgnitionTime(int ignitionTime) {
		this.ignitionTime = ignitionTime;
	}
}
