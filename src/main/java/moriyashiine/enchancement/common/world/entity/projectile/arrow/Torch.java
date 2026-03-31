/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.entity.projectile.arrow;

import moriyashiine.enchancement.common.init.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jspecify.annotations.Nullable;

public class Torch extends AbstractArrow {
	private boolean canFunction = true, shouldPlaceTorch = true;
	private int ignitionTime = 0;

	public Torch(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
	}

	public Torch(Level level, LivingEntity mob, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
		super(ModEntityTypes.TORCH, mob, level, pickupItemStack, firedFromWeapon);
		if (pickup != Pickup.ALLOWED && !(mob instanceof Player player && player.isCreative())) {
			canFunction = false;
		}
		shouldPlaceTorch = mob.isShiftKeyDown();
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return Items.TORCH.getDefaultInstance();
	}

	@Override
	public void tick() {
		super.tick();
		if (level().isClientSide()) {
			level().addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(), 0, 0, 0);
			level().addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0, 0);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
		Entity entity = hitResult.getEntity();
		if (entity instanceof EnderDragonPart part) {
			entity = part.parentMob;
		}
		if (entity instanceof LivingEntity living && entity.getType() != EntityType.ENDERMAN) {
			playSound(SoundEvents.FIRE_EXTINGUISH, 1, 1);
			if (!level().isClientSide()) {
				living.igniteForSeconds(Math.min(16, Mth.ceil(living.getRemainingFireTicks() / 20F) + ignitionTime));
			}
		}
		super.onHitEntity(hitResult);
	}

	@Override
	protected void onHitBlock(BlockHitResult hitResult) {
		BlockState state = level().getBlockState(hitResult.getBlockPos());
		state.onProjectileHit(level(), state, hitResult, this);
		if (level() instanceof ServerLevel level) {
			discard();
			if (canFunction) {
				if (shouldPlaceTorch && getOwner() instanceof Player player && player.getAbilities().mayBuild) {
					BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection());
					BlockPlaceContext context = new BlockPlaceContext(player, InteractionHand.MAIN_HAND, getPickupItem(), hitResult);
					if (context.canPlace() && level().getWorldBorder().isWithinBounds(pos)) {
						state = Blocks.TORCH.getStateForPlacement(context);
						if (state != null && state.canSurvive(level(), pos)) {
							level().setBlockAndUpdate(pos, state);
							playSound(Blocks.TORCH.defaultBlockState().getSoundType().getPlaceSound(), 1, 1);
							return;
						} else {
							state = Blocks.WALL_TORCH.getStateForPlacement(context);
							if (state != null && state.canSurvive(level(), pos)) {
								level().setBlockAndUpdate(pos, state);
								playSound(Blocks.TORCH.defaultBlockState().getSoundType().getPlaceSound(), 1, 1);
								return;
							}
						}
					}
				}
				if (getOwner() instanceof Player player && !player.isCreative()) {
					spawnAtLocation(level, getPickupItem(), 0.1F);
				}
			}
		}
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		canFunction = input.getBooleanOr("CanFunction", false);
		shouldPlaceTorch = input.getBooleanOr("ShouldPlaceTorch", false);
		ignitionTime = input.getIntOr("IgnitionTime", 0);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("CanFunction", canFunction);
		output.putBoolean("ShouldPlaceTorch", shouldPlaceTorch);
		output.putInt("IgnitionTime", ignitionTime);
	}

	@Override
	public boolean isOnFire() {
		return true;
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	public void setIgnitionTime(int ignitionTime) {
		this.ignitionTime = ignitionTime;
	}
}
