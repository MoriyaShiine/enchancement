/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.DashPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.accessor.LivingEntityAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class DashComponent implements AutoSyncedComponent, CommonTickingComponent {
	public static final int DEFAULT_DASH_COOLDOWN = 40;

	private final PlayerEntity obj;
	private boolean shouldRefreshDash = false;
	private int dashCooldown = DEFAULT_DASH_COOLDOWN, lastDashCooldown = DEFAULT_DASH_COOLDOWN, wavedashTicks = 0;

	private int dashLevel = 0;
	private boolean hasDash = false, wasPressingDashKey = false;
	private int ticksPressingJump = 0;

	public DashComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		shouldRefreshDash = tag.getBoolean("ShouldRefreshDash");
		dashCooldown = tag.getInt("DashCooldown");
		lastDashCooldown = tag.getInt("LastDashCooldown");
		wavedashTicks = tag.getInt("WavedashTicks");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("ShouldRefreshDash", shouldRefreshDash);
		tag.putInt("DashCooldown", dashCooldown);
		tag.putInt("LastDashCooldown", lastDashCooldown);
		tag.putInt("WavedashTicks", wavedashTicks);
	}

	@Override
	public void tick() {
		dashLevel = EnchantmentHelper.getEquipmentLevel(ModEnchantments.DASH, obj);
		hasDash = dashLevel > 0;
		if (hasDash) {
			if (!shouldRefreshDash) {
				if (obj.isOnGround()) {
					shouldRefreshDash = true;
				}
			} else if (dashCooldown > 0) {
				dashCooldown--;
			}
			if (wavedashTicks > 0) {
				wavedashTicks--;
			}
		} else {
			shouldRefreshDash = false;
			setDashCooldown(DEFAULT_DASH_COOLDOWN);
			wavedashTicks = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasDash && !obj.isSpectator() && obj == MinecraftClient.getInstance().player) {
			if (((LivingEntityAccessor) obj).enchancement$jumping()) {
				ticksPressingJump = Math.min(2, ++ticksPressingJump);
			} else {
				ticksPressingJump = 0;
			}
			boolean pressingDashKey = EnchancementClient.DASH_KEYBINDING.isPressed();
			if (pressingDashKey && !wasPressingDashKey && canUse()) {
				use();
				if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || obj != MinecraftClient.getInstance().cameraEntity) {
					for (int i = 0; i < 8; i++) {
						obj.getWorld().addParticle(ParticleTypes.CLOUD, obj.getParticleX(1), obj.getRandomBodyY(), obj.getParticleZ(1), 0, 0, 0);
					}
				}
				DashPayload.send();
			}
			wasPressingDashKey = pressingDashKey;
		} else {
			wasPressingDashKey = false;
			ticksPressingJump = 0;
		}
	}

	public int getDashCooldown() {
		return dashCooldown;
	}

	public void setDashCooldown(int dashCooldown) {
		this.dashCooldown = dashCooldown;
		lastDashCooldown = dashCooldown;
	}

	public int getLastDashCooldown() {
		return lastDashCooldown;
	}

	public int getDashLevel() {
		return dashLevel;
	}

	public boolean hasDash() {
		return hasDash;
	}

	public boolean shouldWavedash() {
		return ticksPressingJump < 2 && wavedashTicks > 0 && obj.isOnGround();
	}

	public boolean canUse() {
		return dashCooldown == 0 && !obj.isOnGround() && EnchancementUtil.isGroundedOrAirborne(obj);
	}

	public void use() {
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_DASH, 1, 1);
		Vec3d velocity = obj.getRotationVector().normalize();
		obj.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
		obj.fallDistance = 0;
		setDashCooldown(DEFAULT_DASH_COOLDOWN / dashLevel);
		shouldRefreshDash = false;
		wavedashTicks = 3;
	}
}
