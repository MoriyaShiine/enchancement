/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.StrafePayload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class StrafeComponent implements AutoSyncedComponent, CommonTickingComponent {
	public static final int DEFAULT_STRAFE_COOLDOWN = 40;

	private final PlayerEntity obj;
	private int strafeCooldown = DEFAULT_STRAFE_COOLDOWN, lastStrafeCooldown = DEFAULT_STRAFE_COOLDOWN;

	private int strafeLevel = 0;
	private boolean hasStrafe = false;

	private boolean wasPressingStrafeKey = false;
	private int ticksLeftToPressActivationKey = 0;

	public StrafeComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		strafeCooldown = tag.getInt("StrafeCooldown");
		lastStrafeCooldown = tag.getInt("LastStrafeCooldown");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putInt("StrafeCooldown", strafeCooldown);
		tag.putInt("LastStrafeCooldown", lastStrafeCooldown);
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
			setStrafeCooldown(DEFAULT_STRAFE_COOLDOWN);
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasStrafe && canUse() && obj == MinecraftClient.getInstance().player) {
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
						use(velocity.getX(), velocity.getZ());
						addStrafeParticles(obj);
						StrafePayload.send(velocity);
					}
				} else {
					ticksLeftToPressActivationKey = 7;
				}
			}
			wasPressingStrafeKey = pressingStrafeKey;
		}
	}

	public int getStrafeCooldown() {
		return strafeCooldown;
	}

	public void setStrafeCooldown(int strafeCooldown) {
		this.strafeCooldown = strafeCooldown;
		lastStrafeCooldown = strafeCooldown;
	}

	public int getLastStrafeCooldown() {
		return lastStrafeCooldown;
	}

	public boolean hasStrafe() {
		return hasStrafe;
	}

	public boolean canUse() {
		return strafeCooldown == 0 && !obj.isSpectator();
	}

	public void use(double velocityX, double velocityZ) {
		obj.addVelocity(velocityX, 0, velocityZ);
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_STRAFE, 1, 1);
		setStrafeCooldown(DEFAULT_STRAFE_COOLDOWN / strafeLevel);
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

	public static void addStrafeParticles(Entity entity) {
		if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity) {
			for (int i = 0; i < 8; i++) {
				entity.getWorld().addParticle(ParticleTypes.CLOUD, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
			}
		}
	}
}
