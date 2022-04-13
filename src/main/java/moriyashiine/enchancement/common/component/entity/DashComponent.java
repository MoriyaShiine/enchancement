package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import moriyashiine.enchancement.common.packet.DashPacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class DashComponent implements ClientTickingComponent {
	public static final Object2IntMap<PlayerEntity> PACKET_IMMUNITIES = new Object2IntOpenHashMap<>();

	private final PlayerEntity obj;
	private boolean shouldRefreshDash = false;
	private int dashCooldown = 0, ticksPressingJump = 0, wavedashTicks = 0;

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
	public void clientTick() {
		if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.DASH, obj) > 0) {
			boolean onGround = obj.isOnGround();
			boolean sneaking = obj.isSneaking();
			if (!shouldRefreshDash) {
				if (onGround) {
					shouldRefreshDash = true;
				}
			} else if (dashCooldown > 0) {
				dashCooldown--;
			}
			if (MinecraftClient.getInstance().options.jumpKey.isPressed()) {
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
				DashPacket.send(obj.getRotationVector().normalize());
			}
			wasSneaking = sneaking;
		} else {
			shouldRefreshDash = false;
			dashCooldown = 0;
			ticksPressingJump = 0;
			wavedashTicks = 0;
			wasSneaking = false;
		}
	}

	public boolean shouldWavedash() {
		return ticksPressingJump < 2 && wavedashTicks > 0;
	}

	public int getDashCooldown() {
		return dashCooldown;
	}

	public void setDashCooldown(int dashCooldown) {
		this.dashCooldown = dashCooldown;
	}

	public static void tickPacketImmunities() {
		for (PlayerEntity player : PACKET_IMMUNITIES.keySet()) {
			if (PACKET_IMMUNITIES.put(player, PACKET_IMMUNITIES.getInt(player) - 1) < 0) {
				PACKET_IMMUNITIES.removeInt(player);
			}
		}
	}
}
