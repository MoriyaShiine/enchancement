package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.packet.SyncJumpingPacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.LivingEntityAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class DashComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldRefreshDash = false;
	private int dashCooldown = 20, ticksPressingJump = 0, wavedashTicks = 0;

	private boolean hasDash = false, shouldDash = false, wasSneaking = false;

	public DashComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		shouldRefreshDash = tag.getBoolean("ShouldRefreshDash");
		dashCooldown = tag.getInt("DashCooldown");
		ticksPressingJump = tag.getInt("TicksPressingJump");
		wavedashTicks = tag.getInt("WavedashTicks");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("ShouldRefreshDash", shouldRefreshDash);
		tag.putInt("DashCooldown", dashCooldown);
		tag.putInt("TicksPressingJump", ticksPressingJump);
		tag.putInt("WavedashTicks", wavedashTicks);
	}

	@Override
	public void tick() {
		hasDash = EnchancementUtil.hasEnchantment(ModEnchantments.DASH, obj);
		if (hasDash) {
			boolean onGround = obj.isOnGround();
			boolean sneaking = obj.isSneaking();
			if (!shouldRefreshDash) {
				if (onGround) {
					shouldRefreshDash = true;
				}
			} else if (dashCooldown > 0) {
				dashCooldown--;
			}
			if (ModEntityComponents.JUMPING.get(obj).isJumping()) {
				ticksPressingJump = Math.min(2, ++ticksPressingJump);
			} else {
				ticksPressingJump = 0;
			}
			if (wavedashTicks > 0) {
				wavedashTicks--;
			}
			shouldDash = !onGround && dashCooldown == 0 && sneaking && !wasSneaking && EnchancementUtil.isGroundedOrJumping(obj);
			if (shouldDash) {
				obj.playSound(ModSoundEvents.ENTITY_GENERIC_DASH, 1, 1);
				Vec3d velocity = obj.getRotationVector().normalize();
				obj.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
				obj.fallDistance = 0;
				shouldRefreshDash = false;
				dashCooldown = 20;
				wavedashTicks = 3;
			}
			wasSneaking = sneaking;
		} else {
			shouldRefreshDash = false;
			dashCooldown = 20;
			ticksPressingJump = 0;
			wavedashTicks = 0;
			wasSneaking = false;
			shouldDash = false;
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (shouldDash) {
			EnchancementUtil.PACKET_IMMUNITIES.put(obj, 20);
		}
	}

	@Override
	public void clientTick() {
		tick();
		ModEntityComponents.JUMPING.maybeGet(obj).ifPresent(jumpingComponent -> {
			if (((LivingEntityAccessor) obj).enchancement$jumping()) {
				if (!jumpingComponent.isJumping() && hasDash) {
					SyncJumpingPacket.send(true);
				}
			} else if (jumpingComponent.isJumping()) {
				SyncJumpingPacket.send(false);
			}
		});
		if (shouldDash) {
			if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || obj != MinecraftClient.getInstance().cameraEntity) {
				for (int i = 0; i < 8; i++) {
					obj.world.addParticle(ParticleTypes.CLOUD, obj.getParticleX(1), obj.getRandomBodyY(), obj.getParticleZ(1), 0, 0, 0);
				}
			}
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
}
