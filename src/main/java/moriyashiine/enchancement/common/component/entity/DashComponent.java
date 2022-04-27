package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.packet.DashPacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.LivingEntityAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class DashComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldRefreshDash = false;
	private int dashCooldown = 20, wavedashTicks = 0;

	private boolean hasDash = false, wasSneaking = false;
	private int ticksPressingJump = 0;

	public DashComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		shouldRefreshDash = tag.getBoolean("ShouldRefreshDash");
		dashCooldown = tag.getInt("DashCooldown");
		wavedashTicks = tag.getInt("WavedashTicks");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("ShouldRefreshDash", shouldRefreshDash);
		tag.putInt("DashCooldown", dashCooldown);
		tag.putInt("WavedashTicks", wavedashTicks);
	}

	@Override
	public void tick() {
		hasDash = EnchancementUtil.hasEnchantment(ModEnchantments.DASH, obj);
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
			dashCooldown = 20;
			wavedashTicks = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasDash) {
			if (((LivingEntityAccessor) obj).enchancement$jumping()) {
				ticksPressingJump = Math.min(2, ++ticksPressingJump);
			} else {
				ticksPressingJump = 0;
			}
			boolean sneaking = obj.isSneaking();
			if (!obj.isOnGround() && dashCooldown == 0 && sneaking && !wasSneaking && EnchancementUtil.isGroundedOrJumping(obj)) {
				handle(obj, this);
				addDashParticles(obj);
				DashPacket.send();
			}
			wasSneaking = sneaking;
		} else {
			wasSneaking = false;
			ticksPressingJump = 0;
		}
	}

	public int getDashCooldown() {
		return dashCooldown;
	}

	public void setDashCooldown(int dashCooldown) {
		this.dashCooldown = dashCooldown;
	}

	public boolean hasDash() {
		return hasDash;
	}

	public boolean shouldWavedash() {
		return ticksPressingJump < 2 && wavedashTicks > 0;
	}

	public static void handle(Entity entity, DashComponent dashComponent) {
		entity.playSound(ModSoundEvents.ENTITY_GENERIC_DASH, 1, 1);
		Vec3d velocity = entity.getRotationVector().normalize();
		entity.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
		entity.fallDistance = 0;
		dashComponent.shouldRefreshDash = false;
		dashComponent.dashCooldown = 20;
		dashComponent.wavedashTicks = 3;
	}

	public static void addDashParticles(Entity entity) {
		if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity) {
			for (int i = 0; i < 8; i++) {
				entity.world.addParticle(ParticleTypes.CLOUD, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
			}
		}
	}
}
