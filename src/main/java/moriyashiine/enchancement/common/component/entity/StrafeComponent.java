/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.packet.StrafePacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class StrafeComponent implements AutoSyncedComponent, CommonTickingComponent {
	public static final int DEFAULT_STRAFE_COOLDOWN = 20;

	private final PlayerEntity obj;
	private int strafeCooldown = DEFAULT_STRAFE_COOLDOWN, ticksInAir = 0;

	private boolean hasStrafe = false;

	private boolean wasPressingSpring = false;
	private int ticksLeftToPressSprint = 0;

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
			} else if (EnchancementUtil.isGroundedOrAirborne(obj) && obj.world.raycast(new RaycastContext(obj.getPos(), obj.getPos().add(0, -1, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, obj)).getType() == HitResult.Type.MISS) {
				ticksInAir++;
			}
			if (ticksInAir > 10) {
				obj.airStrafingSpeed *= 2;
			}
		} else {
			strafeCooldown = DEFAULT_STRAFE_COOLDOWN;
			ticksInAir = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasStrafe && strafeCooldown == 0 && !obj.isSpectator()) {
			GameOptions options = MinecraftClient.getInstance().options;
			boolean pressingSprint = options.sprintKey.isPressed();
			if (ticksLeftToPressSprint > 0) {
				ticksLeftToPressSprint--;
			}
			if (pressingSprint && !wasPressingSpring) {
				if (ticksLeftToPressSprint > 0) {
					ticksLeftToPressSprint = 0;
					Vec3d velocity = getVelocityFromInput(options).rotateY((float) Math.toRadians(-(obj.getHeadYaw() + 90)));
					handle(obj, this, velocity.getX(), velocity.getZ());
					addStrafeParticles(obj);
					StrafePacket.send(velocity);
				} else {
					ticksLeftToPressSprint = 7;
				}
			}
			wasPressingSpring = pressingSprint;
		}
	}

	public void setTicksInAir(int ticksInAir) {
		this.ticksInAir = ticksInAir;
	}

	public boolean hasStrafe() {
		return hasStrafe;
	}

	private Vec3d getVelocityFromInput(GameOptions options) {
		if (options.backKey.isPressed()) {
			return new Vec3d(-1, 0, 0);
		}
		if (options.leftKey.isPressed()) {
			return new Vec3d(0, 0, -1);
		}
		if (options.rightKey.isPressed()) {
			return new Vec3d(0, 0, 1);
		}
		return new Vec3d(1, 0, 0);
	}

	public static void handle(Entity entity, StrafeComponent strafeComponent, double velocityX, double velocityZ) {
		entity.addVelocity(velocityX, 0, velocityZ);
		entity.playSound(ModSoundEvents.ENTITY_GENERIC_STRAFE, 1, 1);
		strafeComponent.strafeCooldown = DEFAULT_STRAFE_COOLDOWN;
	}

	public static void addStrafeParticles(Entity entity) {
		if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity) {
			for (int i = 0; i < 8; i++) {
				entity.world.addParticle(ParticleTypes.CLOUD, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
			}
		}
	}
}
