package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import moriyashiine.enchancement.common.packet.SyncDashPacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class DashComponent implements ClientTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldRefreshDash = false;
	private int dashCooldown = 0, wavedashTimer = 0;

	private boolean wasSneaking = false;

	public DashComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		shouldRefreshDash = tag.getBoolean("ShouldRefreshDash");
		dashCooldown = tag.getInt("DashCooldown");
		wavedashTimer = tag.getInt("WavedashTimer");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("ShouldRefreshDash", shouldRefreshDash);
		tag.putInt("DashCooldown", dashCooldown);
		tag.putInt("WavedashTimer", wavedashTimer);
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
			if (wavedashTimer > 0) {
				wavedashTimer--;
			}
			if (!onGround && dashCooldown == 0 && sneaking && !wasSneaking) {
				shouldRefreshDash = false;
				dashCooldown = 20;
				wavedashTimer = 3;
				obj.setVelocity(obj.getRotationVector().normalize());
				obj.fallDistance = 0;
				SyncDashPacket.send(true, obj.getVelocity());
			}
			wasSneaking = sneaking;
		}
	}

	public boolean shouldWavedash() {
		return wavedashTimer > 0;
	}

	public int getDashCooldown() {
		return dashCooldown;
	}

	public void setDashCooldown(int dashCooldown) {
		this.dashCooldown = dashCooldown;
	}
}
