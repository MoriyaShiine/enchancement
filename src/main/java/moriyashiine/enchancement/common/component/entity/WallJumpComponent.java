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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class WallJumpComponent implements AutoSyncedComponent, CommonTickingComponent {
	private static final Identifier SAFE_FALL_DISTANCE_ID = Enchancement.id("wall_jump_safe_fall_distance");

	private final LivingEntity obj;
	private BlockPos slidingPos = null;

	private final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
	private float jumpStrength = 0;
	private boolean hasJumped = false;

	public WallJumpComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		slidingPos = input.read("SlidingPos", BlockPos.CODEC).orElse(null);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.storeNullable("SlidingPos", BlockPos.CODEC, slidingPos);
	}

	@Override
	public void tick() {
		jumpStrength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.WALL_JUMP, obj, 0);
		if (jumpStrength == 0 || obj.onGround()) {
			slidingPos = null;
			hasJumped = false;
		} else if (slidingPos != null) {
			((HoneyBlock) Blocks.HONEY_BLOCK).maybeDoSlideEffects(obj.level(), obj);
			((HoneyBlock) Blocks.HONEY_BLOCK).doSlideMovement(obj);
		}
	}

	@Override
	public void serverTick() {
		tick();
		AttributeInstance safeFallDistance = obj.getAttribute(Attributes.SAFE_FALL_DISTANCE);
		if (hasJumped()) {
			if (!safeFallDistance.hasModifier(SAFE_FALL_DISTANCE_ID)) {
				int reduction = Mth.floor(MultiplyMovementSpeedEvent.getJumpStrength(obj, jumpStrength * 3) * 10);
				safeFallDistance.addPermanentModifier(new AttributeModifier(SAFE_FALL_DISTANCE_ID, reduction, AttributeModifier.Operation.ADD_VALUE));
			}
		} else if (safeFallDistance.hasModifier(SAFE_FALL_DISTANCE_ID)) {
			safeFallDistance.removeModifier(SAFE_FALL_DISTANCE_ID);
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
						BlockState state = obj.level().getBlockState(mutable);
						if (!state.is(ModBlockTags.UNSTICKABLE) && !state.getCollisionShape(obj.level(), mutable, CollisionContext.of(obj)).isEmpty() && !obj.onGround() && obj.getDeltaMovement().y() < -0.1) {
							double mutableDistance = mutable.distToCenterSqr(obj.position());
							if (targetPos != null && mutableDistance == targetPos.distToCenterSqr(obj.position())) {
								BlockPos p1 = mutable.relative(direction.getClockWise());
								BlockPos p2 = mutable.relative(direction.getCounterClockWise());
								if (p1.distToCenterSqr(obj.position()) < p2.distToCenterSqr(obj.position())) {
									targetPos = p1;
								} else {
									targetPos = p2;
								}
							} else if (targetPos == null || mutableDistance < targetPos.distToCenterSqr(obj.position())) {
								targetPos = mutable.immutable();
							}
						}
						mutable.move(direction.getOpposite());
					}
				}
			}
			if (slidingPos != null && (obj.getControllingPassenger() instanceof Player player ? player : obj).jumping) {
				Vec3 diff = obj.blockPosition().getCenter().subtract(slidingPos.getCenter()).normalize().scale(jumpStrength);
				Vec3 delta = new Vec3(diff.x(), MultiplyMovementSpeedEvent.getJumpStrength(obj, jumpStrength * (obj instanceof Player ? 3 : 2)), diff.z());
				use(delta);
				WallJumpPayload.send(obj, delta);
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

	public void use(Vec3 delta) {
		obj.setDeltaMovement(delta);
		obj.playSound(SoundEvents.SLIME_BLOCK_FALL);
		obj.gameEvent(GameEvent.ENTITY_ACTION);
		slidingPos = null;
		hasJumped = true;
	}
}
