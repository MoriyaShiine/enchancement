package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.packet.StrafePacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.RaycastContext;

public class StrafeComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private int strafeCooldown = 0, ticksInAir = 0;
	private boolean wasPressingForward = false, wasPressingBackward = false, wasPressingLeft = false, wasPressingRight = false;
	private int ticksLeftToPressForward = 0, ticksLeftToPressBackward = 0, ticksLeftToPressLeft = 0, ticksLeftToPressRight = 0;

	private boolean hasStrafe = false;

	public StrafeComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		strafeCooldown = tag.getInt("StrafeCooldown");
		ticksInAir = tag.getInt("TicksInAir");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("StrafeCooldown", strafeCooldown);
		tag.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void tick() {
		hasStrafe = EnchancementUtil.hasEnchantment(ModEnchantments.STRAFE, obj);
		if (hasStrafe) {
			if (strafeCooldown > 0) {
				strafeCooldown--;
			}
			if (obj.isOnGround()) {
				ticksInAir = 0;
			} else if (EnchancementUtil.isGroundedOrJumping(obj) && obj.world.raycast(new RaycastContext(obj.getPos(), obj.getPos().add(0, -1, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, obj)).getType() == HitResult.Type.MISS) {
				ticksInAir++;
			}
			if (ticksInAir > 10) {
				obj.airStrafingSpeed *= MathHelper.lerp(MathHelper.clamp((ticksInAir - 10) / 20F, 0, 1), 0, 10);
			}
		} else {
			strafeCooldown = 0;
			ticksInAir = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasStrafe && strafeCooldown == 0 && obj == MinecraftClient.getInstance().player) {
			Direction direction = updatePressing();
			if (direction != null) {
				float pitch = obj.getPitch();
				float yaw = obj.getHeadYaw();
				switch (direction) {
					case SOUTH -> pitch += 90;
					case WEST -> yaw += 270;
					case EAST -> yaw += 90;
				}
				float boostX = (float) (-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
				float boostZ = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
				Vec2f boost = new Vec2f(boostX, boostZ).normalize().multiply(0.75F);
				handle(obj, this, boost.x, boost.y);
				addStrafeParticles(obj);
				StrafePacket.send(boost);
			}
		}
	}

	public boolean hasStrafe() {
		return hasStrafe;
	}

	private Direction updatePressing() {
		Direction direction = null;
		boolean pressingForward = MinecraftClient.getInstance().options.forwardKey.isPressed();
		boolean pressingBackward = MinecraftClient.getInstance().options.backKey.isPressed();
		boolean pressingLeft = MinecraftClient.getInstance().options.leftKey.isPressed();
		boolean pressingRight = MinecraftClient.getInstance().options.rightKey.isPressed();
		if (ticksLeftToPressForward > 0) {
			ticksLeftToPressForward--;
		}
		if (ticksLeftToPressBackward > 0) {
			ticksLeftToPressBackward--;
		}
		if (ticksLeftToPressLeft > 0) {
			ticksLeftToPressLeft--;
		}
		if (ticksLeftToPressRight > 0) {
			ticksLeftToPressRight--;
		}
		if (pressingForward && !wasPressingForward) {
			if (ticksLeftToPressForward > 0) {
				ticksLeftToPressForward = 0;
				direction = Direction.NORTH;
			} else {
				ticksLeftToPressForward = 7;
			}
		} else if (pressingBackward && !wasPressingBackward) {
			if (ticksLeftToPressBackward > 0) {
				ticksLeftToPressBackward = 0;
				direction = Direction.SOUTH;
			} else {
				ticksLeftToPressBackward = 7;
			}
		} else if (pressingLeft && !wasPressingLeft) {
			if (ticksLeftToPressLeft > 0) {
				ticksLeftToPressLeft = 0;
				direction = Direction.WEST;
			} else {
				ticksLeftToPressLeft = 7;
			}
		} else if (pressingRight && !wasPressingRight) {
			if (ticksLeftToPressRight > 0) {
				ticksLeftToPressRight = 0;
				direction = Direction.EAST;
			} else {
				ticksLeftToPressRight = 7;
			}
		}
		wasPressingForward = pressingForward;
		wasPressingBackward = pressingBackward;
		wasPressingLeft = pressingLeft;
		wasPressingRight = pressingRight;
		return direction;
	}

	public static void handle(Entity entity, StrafeComponent strafeComponent, float boostX, float boostZ) {
		entity.addVelocity(boostX, 0, boostZ);
		entity.playSound(ModSoundEvents.ENTITY_GENERIC_STRAFE, 1, 1);
		strafeComponent.strafeCooldown = 20;
	}

	public static void addStrafeParticles(Entity entity) {
		if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity) {
			for (int i = 0; i < 8; i++) {
				entity.world.addParticle(ParticleTypes.CLOUD, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
			}
		}
	}
}
