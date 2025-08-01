/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.payload.WallJumpPayload;
import moriyashiine.enchancement.common.payload.WallJumpSlidingPayload;
import moriyashiine.enchancement.common.tag.ModBlockTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HoneyBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class WallJumpComponent implements AutoSyncedComponent, CommonTickingComponent {
	private static final Identifier SAFE_FALL_DISTANCE_ID = Enchancement.id("wall_jump_safe_fall_distance");

	private final LivingEntity obj;
	private BlockPos slidingPos = null;

	private final BlockPos.Mutable mutable = new BlockPos.Mutable();
	private float jumpStrength = 0;
	private boolean hasJumped = false;

	public WallJumpComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		slidingPos = readView.read("SlidingPos", BlockPos.CODEC).orElse(null);
	}

	@Override
	public void writeData(WriteView writeView) {
		if (slidingPos != null) {
			writeView.put("SlidingPos", BlockPos.CODEC, slidingPos);
		}
	}

	@Override
	public void tick() {
		jumpStrength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.WALL_JUMP, obj, 0);
		if (jumpStrength == 0 || obj.isOnGround()) {
			slidingPos = null;
			hasJumped = false;
		} else if (slidingPos != null) {
			((HoneyBlock) Blocks.HONEY_BLOCK).addCollisionEffects(obj.getWorld(), obj);
			((HoneyBlock) Blocks.HONEY_BLOCK).updateSlidingVelocity(obj);
		}
	}

	@Override
	public void serverTick() {
		tick();
		EntityAttributeInstance safeFallDistanceAttribute = obj.getAttributeInstance(EntityAttributes.SAFE_FALL_DISTANCE);
		if (hasJumped()) {
			if (!safeFallDistanceAttribute.hasModifier(SAFE_FALL_DISTANCE_ID)) {
				int reduction = MathHelper.floor(MultiplyMovementSpeedEvent.getJumpStrength(obj, jumpStrength * 3) * 10);
				safeFallDistanceAttribute.addPersistentModifier(new EntityAttributeModifier(SAFE_FALL_DISTANCE_ID, reduction, EntityAttributeModifier.Operation.ADD_VALUE));
			}
		} else if (safeFallDistanceAttribute.hasModifier(SAFE_FALL_DISTANCE_ID)) {
			safeFallDistanceAttribute.removeModifier(SAFE_FALL_DISTANCE_ID);
		}
	}

	@Override
	public void clientTick() {
		tick();
		BlockPos targetPos = null;
		if (jumpStrength != 0) {
			if (obj.horizontalCollision) {
				mutable.set(obj.getX(), obj.getY(), obj.getZ());
				for (Direction direction : Direction.values()) {
					if (direction.getAxis().isHorizontal()) {
						mutable.move(direction);
						BlockState state = obj.getWorld().getBlockState(mutable);
						if (!state.isIn(ModBlockTags.UNSTICKABLE) && !state.getCollisionShape(obj.getWorld(), mutable, ShapeContext.of(obj)).isEmpty() && !obj.isOnGround() && obj.getVelocity().getY() < -0.1) {
							double mutableDistance = mutable.getSquaredDistance(obj.getPos());
							if (targetPos != null && mutableDistance == targetPos.getSquaredDistance(obj.getPos())) {
								BlockPos p1 = mutable.offset(direction.rotateYClockwise());
								BlockPos p2 = mutable.offset(direction.rotateYCounterclockwise());
								if (p1.getSquaredDistance(obj.getPos()) < p2.getSquaredDistance(obj.getPos())) {
									targetPos = p1;
								} else {
									targetPos = p2;
								}
							} else if (targetPos == null || mutableDistance < targetPos.getSquaredDistance(obj.getPos())) {
								targetPos = mutable.toImmutable();
							}
						}
						mutable.move(direction.getOpposite());
					}
				}
			}
			if (slidingPos != null && (obj.getControllingPassenger() instanceof PlayerEntity player ? player : obj).jumping) {
				Vec3d diff = obj.getBlockPos().toCenterPos().subtract(slidingPos.toCenterPos()).normalize().multiply(jumpStrength);
				Vec3d velocity = new Vec3d(diff.getX(), MultiplyMovementSpeedEvent.getJumpStrength(obj, jumpStrength * (obj instanceof PlayerEntity ? 3 : 2)), diff.getZ());
				use(velocity);
				WallJumpPayload.send(obj, velocity);
				targetPos = null;
			}
		}
		if (slidingPos != targetPos) {
			slidingPos = targetPos;
			WallJumpSlidingPayload.send(obj, slidingPos);
		}
	}

	public boolean hasJumped() {
		return hasJumped;
	}

	public void setSlidingPos(BlockPos slidingPos) {
		this.slidingPos = slidingPos;
	}

	public boolean isSliding() {
		return slidingPos != null;
	}

	public void use(Vec3d velocity) {
		obj.setVelocity(velocity);
		obj.playSound(SoundEvents.BLOCK_SLIME_BLOCK_FALL);
		obj.emitGameEvent(GameEvent.ENTITY_ACTION);
		slidingPos = null;
		hasJumped = true;
	}
}
