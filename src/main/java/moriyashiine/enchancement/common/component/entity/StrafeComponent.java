/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.packet.StrafePacket;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class StrafeComponent implements AutoSyncedComponent, CommonTickingComponent {
	public static final int DEFAULT_STRAFE_COOLDOWN = 20;

	private final PlayerEntity obj;
	private int strafeCooldown = DEFAULT_STRAFE_COOLDOWN;

	private boolean hasStrafe = false;

	private boolean wasPressingActivationKey = false;
	private int ticksLeftToPressActivationKey = 0;

	public StrafeComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		strafeCooldown = tag.getInt("StrafeCooldown");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("StrafeCooldown", strafeCooldown);
	}

	@Override
	public void tick() {
		hasStrafe = EnchancementUtil.hasEnchantment(ModEnchantments.STRAFE, obj);
		if (hasStrafe) {
			if (strafeCooldown > 0) {
				strafeCooldown--;
			}
		} else {
			strafeCooldown = DEFAULT_STRAFE_COOLDOWN;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasStrafe && strafeCooldown == 0 && !obj.isSpectator() && obj == MinecraftClient.getInstance().player) {
			GameOptions options = MinecraftClient.getInstance().options;
			boolean pressingActivationKey = EnchancementClient.STRAFE_KEYBINDING.isUnbound() ? options.sprintKey.isPressed() : EnchancementClient.STRAFE_KEYBINDING.isPressed();
			if (ticksLeftToPressActivationKey > 0) {
				ticksLeftToPressActivationKey--;
			}
			if (pressingActivationKey && !wasPressingActivationKey) {
				if (ticksLeftToPressActivationKey > 0) {
					ticksLeftToPressActivationKey = 0;
					Vec3d velocity = getVelocityFromInput(options).rotateY((float) Math.toRadians(-(obj.getHeadYaw() + 90)));
					handle(obj, this, velocity.getX(), velocity.getZ());
					addStrafeParticles(obj);
					StrafePacket.send(velocity);
				} else {
					ticksLeftToPressActivationKey = 7;
				}
			}
			wasPressingActivationKey = pressingActivationKey;
		}
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
				entity.getWorld().addParticle(ParticleTypes.CLOUD, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
			}
		}
	}
}
