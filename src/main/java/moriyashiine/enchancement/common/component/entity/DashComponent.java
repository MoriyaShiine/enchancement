package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import moriyashiine.enchancement.client.packet.AddDashParticlesPacket;
import moriyashiine.enchancement.common.packet.SyncJumpingPacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.LivingEntityAccessor;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class DashComponent implements CommonTickingComponent {
	public static final Object2IntMap<PlayerEntity> PACKET_IMMUNITIES = new Object2IntOpenHashMap<>();

	private final PlayerEntity obj;
	private boolean shouldRefreshDash = false;
	private int dashCooldown = 20, ticksPressingJump = 0, wavedashTicks = 0;

	private boolean wasSneaking = false;

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
		boolean hasDash = EnchantmentHelper.getEquipmentLevel(ModEnchantments.DASH, obj) > 0;
		if (obj.world.isClient) {
			ModEntityComponents.JUMPING.maybeGet(obj).ifPresent(jumpingComponent -> {
				if (((LivingEntityAccessor) obj).enchancement$jumping()) {
					if (!jumpingComponent.isJumping() && hasDash) {
						SyncJumpingPacket.send(true);
					}
				} else if (jumpingComponent.isJumping()) {
					SyncJumpingPacket.send(false);
				}
			});
		}
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
			if (!onGround && dashCooldown == 0 && sneaking && !wasSneaking && EnchancementUtil.isGroundedOrJumping(obj)) {
				shouldRefreshDash = false;
				dashCooldown = 20;
				wavedashTicks = 3;
				if (!obj.world.isClient) {
					PlayerLookup.tracking(obj).forEach(foundPlayer -> AddDashParticlesPacket.send(foundPlayer, obj));
					AddDashParticlesPacket.send((ServerPlayerEntity) obj, obj);
					obj.world.playSoundFromEntity(null, obj, ModSoundEvents.ENTITY_GENERIC_DASH, obj.getSoundCategory(), 1, 1);
					Vec3d velocity = obj.getRotationVector().normalize();
					obj.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
					obj.velocityModified = true;
					obj.fallDistance = 0;
					PACKET_IMMUNITIES.put(obj, 5);
				}
			}
			wasSneaking = sneaking;
		} else {
			shouldRefreshDash = false;
			dashCooldown = 20;
			ticksPressingJump = 0;
			wavedashTicks = 0;
			wasSneaking = false;
		}
	}

	public boolean shouldRefreshDash() {
		return shouldRefreshDash;
	}

	public int getDashCooldown() {
		return dashCooldown;
	}

	public void setDashCooldown(int dashCooldown) {
		this.dashCooldown = dashCooldown;
	}

	public boolean shouldWavedash() {
		return ticksPressingJump < 2 && wavedashTicks > 0;
	}

	public static void tickPacketImmunities() {
		for (PlayerEntity player : PACKET_IMMUNITIES.keySet()) {
			if (PACKET_IMMUNITIES.put(player, PACKET_IMMUNITIES.getInt(player) - 1) < 0) {
				PACKET_IMMUNITIES.removeInt(player);
			}
		}
	}
}
