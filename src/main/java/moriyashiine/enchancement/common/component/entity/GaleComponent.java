package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import moriyashiine.enchancement.common.packet.GaleJumpPacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class GaleComponent implements ClientTickingComponent {
	private final PlayerEntity obj;
	private int jumpCooldown = 0, ticksInAir = 0, timesJumped = 0;

	public GaleComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		jumpCooldown = tag.getInt("JumpCooldown");
		timesJumped = tag.getInt("TimesJumped");
		ticksInAir = tag.getInt("TicksInAir");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("JumpCooldown", jumpCooldown);
		tag.putInt("TicksInAir", ticksInAir);
		tag.putInt("TimesJumped", timesJumped);
	}

	@Override
	public void clientTick() {
		boolean onGround = obj.isOnGround();
		if (!onGround) {
			ticksInAir++;
		} else {
			ticksInAir = 0;
		}
		if (jumpCooldown == 0) {
			if (!onGround) {
				if (ticksInAir >= 10 && timesJumped < 2 && MinecraftClient.getInstance().options.jumpKey.isPressed() && EnchancementUtil.isGroundedOrJumping(obj) && EnchantmentHelper.getEquipmentLevel(ModEnchantments.GALE, obj) > 0) {
					jumpCooldown = 10;
					timesJumped++;
					GaleJumpPacket.send();
				}
			} else {
				timesJumped = 0;
			}
		} else if (jumpCooldown > 0) {
			jumpCooldown--;
		}
	}
}
