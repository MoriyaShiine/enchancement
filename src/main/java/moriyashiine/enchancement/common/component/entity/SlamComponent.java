/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

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
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import moriyashiine.strawberrylib.api.objects.records.ParticleVelocity;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.Thickness;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.*;
import net.minecraft.world.event.GameEvent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class SlamComponent implements CommonTickingComponent {
	public static final int DEFAULT_SLAM_COOLDOWN = 7;

	private final PlayerEntity obj;
	private boolean isSlamming = false;
	private int slamCooldown = DEFAULT_SLAM_COOLDOWN, ticksLeftToJump = 0;

	private float strength = 0;
	private boolean hasSlam = false;

	private boolean wasPressingKey = false;

	public SlamComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		isSlamming = readView.getBoolean("IsSlamming", false);
		slamCooldown = readView.getInt("SlamCooldown", 0);
		ticksLeftToJump = readView.getInt("TicksLeftToJump", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.putBoolean("IsSlamming", isSlamming);
		writeView.putInt("SlamCooldown", slamCooldown);
		writeView.putInt("TicksLeftToJump", ticksLeftToJump);
	}

	@Override
	public void tick() {
		strength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.SLAM, obj, 0);
		hasSlam = strength > 0;
		if (hasSlam) {
			if (isSlamming) {
				if (!SLibUtils.isGroundedOrAirborne(obj, true)) {
					isSlamming = false;
					return;
				}
				obj.setVelocity(obj.getVelocity().getX() * 0.98, Math.min(-3, obj.getVelocity().getY()), obj.getVelocity().getZ() * 0.98);
				EnchancementUtil.resetFallDistance(obj);
			}
			if (slamCooldown > 0) {
				slamCooldown--;
			}
			if (ticksLeftToJump > 0) {
				ticksLeftToJump--;
			}
		} else {
			isSlamming = false;
			slamCooldown = DEFAULT_SLAM_COOLDOWN;
			ticksLeftToJump = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasSlam) {
			if (isSlamming) {
				SLibClientUtils.addParticles(obj, ModParticleTypes.VELOCITY_LINE, 4, ParticleAnchor.BODY, ParticleVelocity.of(new Vec3d(0, 1, 0)));
			}
			if (!obj.isSpectator() && SLibClientUtils.isHost(obj)) {
				if (isSlamming && obj.isOnGround()) {
					stopSlammingClient(obj.getY());
					StopSlammingC2SPayload.send(obj.getY());
				}
				boolean pressingKey = EnchancementClient.SLAM_KEYBINDING.isPressed();
				if (pressingKey && !wasPressingKey && canSlam()) {
					isSlamming = true;
					slamCooldown = DEFAULT_SLAM_COOLDOWN;
					StartSlammingC2SPayload.send();
				}
				wasPressingKey = pressingKey;
			}
		}
	}

	public void setSlamming(boolean slamming) {
		this.isSlamming = slamming;
	}

	public boolean isSlamming() {
		return isSlamming;
	}

	public void setSlamCooldown(int slamCooldown) {
		this.slamCooldown = slamCooldown;
	}

	public float getJumpBoostStrength() {
		if (ticksLeftToJump > 0) {
			return MultiplyMovementSpeedEvent.getJumpStrength(obj, (1 + strength) / obj.getJumpVelocity());
		}
		return 1;
	}

	public boolean hasSlam() {
		return hasSlam;
	}

	public boolean canSlam() {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(obj);
		if (slideComponent != null && slideComponent.isSliding()) {
			return false;
		}
		return slamCooldown == 0 && !obj.isOnGround() && SLibUtils.isGroundedOrAirborne(obj);
	}

	private void stopSlamming() {
		isSlamming = false;
		ticksLeftToJump = 5;
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_IMPACT, 1, 1);
	}

	public void stopSlammingServer() {
		stopSlamming();
		obj.getWorld().getOtherEntities(obj, new Box(obj.getBlockPos()).expand(3, 1, 3), foundEntity -> foundEntity.isAlive() && foundEntity.distanceTo(obj) < 5).forEach(foundEntity -> {
			if (foundEntity instanceof LivingEntity living && SLibUtils.shouldHurt(obj, living) && SLibUtils.canSee(obj, foundEntity, 0)) {
				living.takeKnockback(1, obj.getX() - living.getX(), obj.getZ() - living.getZ());
			}
		});
		obj.getWorld().emitGameEvent(GameEvent.STEP, obj.getPos(), GameEvent.Emitter.of(obj.getSteppingBlockState()));
		@SuppressWarnings("deprecation") BlockState state = obj.getWorld().getBlockState(obj.getLandingPos());
		if (state.contains(Properties.THICKNESS) && state.contains(Properties.VERTICAL_DIRECTION) && state.get(Properties.THICKNESS) == Thickness.TIP && state.get(Properties.VERTICAL_DIRECTION) == Direction.UP) {
			obj.damage((ServerWorld) obj.getWorld(), obj.getDamageSources().stalagmite(), Integer.MAX_VALUE);
		}
	}

	public void stopSlammingClient(double posY) {
		stopSlamming();
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		double y = Math.round(posY - 1);
		for (int i = 0; i < 360; i += 15) {
			for (int j = 1; j < 5; j++) {
				double x = obj.getX() + MathHelper.sin(i) * j / 2, z = obj.getZ() + MathHelper.cos(i) * j / 2;
				BlockState state = obj.getWorld().getBlockState(mutable.set(x, y, z));
				if (!state.isReplaceable() && obj.getWorld().getBlockState(mutable.move(Direction.UP)).isReplaceable()) {
					obj.getWorld().addParticleClient(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), x, mutable.getY(), z, 0, 0, 0);
				}
			}
		}
	}
}
