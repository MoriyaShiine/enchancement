/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModParticleTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.StartSlammingC2SPayload;
import moriyashiine.enchancement.common.payload.StopSlammingC2SPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class SlamComponent implements CommonTickingComponent {
	private static final int DEFAULT_SLAM_COOLDOWN = 7;

	private final Player obj;
	private boolean isSlamming = false;
	private int slamCooldown = DEFAULT_SLAM_COOLDOWN, ticksLeftToJump = 0, slamStorageTicks = 0;

	private float strength = 0;

	private boolean wasPressingKey = false;

	public SlamComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		isSlamming = input.getBooleanOr("IsSlamming", false);
		slamCooldown = input.getIntOr("SlamCooldown", 0);
		ticksLeftToJump = input.getIntOr("TicksLeftToJump", 0);
		slamStorageTicks = input.getIntOr("SlamStorageTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("IsSlamming", isSlamming);
		output.putInt("SlamCooldown", slamCooldown);
		output.putInt("TicksLeftToJump", ticksLeftToJump);
		output.putInt("SlamStorageTicks", slamStorageTicks);
	}

	@Override
	public void tick() {
		strength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.SLAM, obj, 0);
		if (hasSlam()) {
			if (isSlamming) {
				if (obj.hurtTime != 0 || !SLibUtils.isGroundedOrAirborne(obj, true)) {
					setSlamming(false);
					return;
				}
				if (slamStorageTicks == 0) {
					obj.setDeltaMovement(obj.getDeltaMovement().x() * 0.98, Math.min(-3, obj.getDeltaMovement().y()), obj.getDeltaMovement().z() * 0.98);
				}
				EnchancementUtil.resetFallDistance(obj);
			}
			if (slamCooldown > 0) {
				slamCooldown--;
			}
			if (slamStorageTicks > 0 && slamStorageTicks < 80) {
				slamStorageTicks++;
			}
			if (ticksLeftToJump > 0 && obj.onGround()) {
				ticksLeftToJump--;
				if (ticksLeftToJump == 0) {
					slamStorageTicks = 0;
				}
			}
		} else {
			setSlamming(false);
			resetCooldowns();
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasSlam()) {
			if (isSlamming) {
				for (int i = 0; i < (SLibClientUtils.shouldAddParticles(obj) ? 4 : 1); i++) {
					obj.level().addParticle(ModParticleTypes.VELOCITY_LINE, obj.getRandomX(1), obj.getRandomY(), obj.getRandomZ(1), 0, 1, 0);
				}
			}
			if (!obj.isSpectator() && SLibClientUtils.isHost(obj)) {
				if (isSlamming && obj.onGround()) {
					stopSlammingClient(obj.getY());
					StopSlammingC2SPayload.send(obj.getY());
				}
				boolean pressingKey = EnchancementClient.SLAM_KEYMAPPING.isDown();
				if (pressingKey && !wasPressingKey && canSlam()) {
					setSlamming(true);
					StartSlammingC2SPayload.send();
				}
				wasPressingKey = pressingKey;
			}
		}
	}

	public void setSlamming(boolean slamming) {
		this.isSlamming = slamming;
		if (slamming) {
			resetCooldowns();
		}
	}

	public boolean isSlamming() {
		return isSlamming;
	}

	public void resetCooldowns() {
		slamCooldown = DEFAULT_SLAM_COOLDOWN;
		ticksLeftToJump = 0;
		slamStorageTicks = 0;
	}

	public void startSlamStorage() {
		if (slamStorageTicks == 0) {
			slamStorageTicks = 1;
		}
	}

	public float getJumpBoostStrength() {
		if (ticksLeftToJump > 0) {
			float boost = MultiplyMovementSpeedEvent.getJumpStrength(obj, (1 + strength + (slamStorageTicks / 12F)) / obj.getJumpPower());
			ticksLeftToJump = slamStorageTicks = 0;
			return boost;
		}
		return 1;
	}

	public boolean hasSlam() {
		return strength > 0;
	}

	public boolean canSlam() {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(obj);
		if (slideComponent != null && slideComponent.isSliding()) {
			return false;
		}
		return slamCooldown == 0 && !obj.onGround() && SLibUtils.isGroundedOrAirborne(obj);
	}

	private void stopSlamming() {
		setSlamming(false);
		ticksLeftToJump = 5;
		obj.playSound(ModSoundEvents.GENERIC_IMPACT, 1, 1);
	}

	public void stopSlammingServer() {
		stopSlamming();
		obj.level().getEntities(obj, new AABB(obj.blockPosition()).inflate(3, 1, 3), foundEntity -> foundEntity.isAlive() && foundEntity.distanceTo(obj) < 5).forEach(foundEntity -> {
			if (foundEntity instanceof LivingEntity living && SLibUtils.shouldHurt(obj, living) && SLibUtils.hasLineOfSight(obj, foundEntity, 0)) {
				living.knockback(1, obj.getX() - living.getX(), obj.getZ() - living.getZ());
			}
		});
		obj.level().gameEvent(GameEvent.STEP, obj.position(), GameEvent.Context.of(obj.getBlockStateOn()));
		@SuppressWarnings("deprecation") BlockState state = obj.level().getBlockState(obj.getOnPosLegacy());
		if (state.hasProperty(BlockStateProperties.DRIPSTONE_THICKNESS) && state.hasProperty(BlockStateProperties.VERTICAL_DIRECTION) && state.getValue(BlockStateProperties.DRIPSTONE_THICKNESS) == DripstoneThickness.TIP && state.getValue(BlockStateProperties.VERTICAL_DIRECTION) == Direction.UP) {
			obj.hurtServer((ServerLevel) obj.level(), obj.damageSources().stalagmite(), Integer.MAX_VALUE);
		}
	}

	public void stopSlammingClient(double posY) {
		stopSlamming();
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		double y = Math.round(posY - 1);
		for (int i = 0; i < 360; i += 15) {
			for (int j = 1; j < 5; j++) {
				double x = obj.getX() + Mth.sin(i) * j / 2, z = obj.getZ() + Mth.cos(i) * j / 2;
				BlockState state = obj.level().getBlockState(mutable.set(x, y, z));
				if (!state.canBeReplaced() && obj.level().getBlockState(mutable.move(Direction.UP)).canBeReplaced()) {
					obj.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), x, mutable.getY(), z, 0, 0, 0);
				}
			}
		}
	}
}
