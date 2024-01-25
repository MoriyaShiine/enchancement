/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.packet.StrafePacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class StrafeComponent implements AutoSyncedComponent, CommonTickingComponent {
	public static final int DEFAULT_STRAFE_COOLDOWN = 40;

	private final PlayerEntity obj;
	private int strafeCooldown = DEFAULT_STRAFE_COOLDOWN;

	private int strafeLevel = 0;
	private boolean hasStrafe = false;

	private boolean wasPressingStrafeKey = false;
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
		strafeLevel = EnchantmentHelper.getEquipmentLevel(ModEnchantments.STRAFE, obj);
		hasStrafe = strafeLevel > 0;
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
			boolean pressingStrafeKey = EnchancementClient.STRAFE_KEYBINDING.isPressed();
			if (ticksLeftToPressActivationKey > 0) {
				ticksLeftToPressActivationKey--;
			}
			if (pressingStrafeKey && !wasPressingStrafeKey) {
				if (ticksLeftToPressActivationKey > 0 || ModConfig.singlePressStrafe) {
					ticksLeftToPressActivationKey = 0;
					Vec3d inputVelocity = getVelocityFromInput(options);
					if (inputVelocity != Vec3d.ZERO) {
						Vec3d velocity = inputVelocity.rotateY((float) Math.toRadians(-(obj.getHeadYaw() + 90)));
						handle(obj, this, velocity.getX(), velocity.getZ());
						addStrafeParticles(obj);
						StrafePacket.send(velocity);
					}
				} else {
					ticksLeftToPressActivationKey = 7;
				}
			}
			wasPressingStrafeKey = pressingStrafeKey;
		}
	}

	public boolean hasStrafe() {
		return hasStrafe;
	}

	private Vec3d getVelocityFromInput(GameOptions options) {
		if (options.forwardKey.isPressed()) {
			return new Vec3d(1, 0, 0);
		}
		if (options.backKey.isPressed()) {
			return new Vec3d(-1, 0, 0);
		}
		if (options.leftKey.isPressed()) {
			return new Vec3d(0, 0, -1);
		}
		if (options.rightKey.isPressed()) {
			return new Vec3d(0, 0, 1);
		}
		return Vec3d.ZERO;
	}

	public static void handle(PlayerEntity player, StrafeComponent strafeComponent, double velocityX, double velocityZ) {
		player.addVelocity(velocityX, 0, velocityZ);
		player.playSound(ModSoundEvents.ENTITY_GENERIC_STRAFE, 1, 1);
		strafeComponent.strafeCooldown = DEFAULT_STRAFE_COOLDOWN / strafeComponent.strafeLevel;
	}

	public static void addStrafeParticles(Entity entity) {
		if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity) {
			for (int i = 0; i < 8; i++) {
				entity.getWorld().addParticle(ParticleTypes.CLOUD, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
			}
		}
	}
}
