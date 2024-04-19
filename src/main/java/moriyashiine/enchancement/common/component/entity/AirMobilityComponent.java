/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;

public class AirMobilityComponent implements CommonTickingComponent {
	private final LivingEntity obj;
	private int resetBypassTicks = 0, ticksInAir = 0;

	public AirMobilityComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		resetBypassTicks = tag.getInt("ResetBypassTicks");
		ticksInAir = tag.getInt("TicksInAir");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("ResetBypassTicks", resetBypassTicks);
		tag.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void tick() {
		if (ModConfig.enchantedChestplatesIncreaseAirMobility && obj.getEquippedStack(EquipmentSlot.CHEST).hasEnchantments() && !(obj.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ElytraItem)) {
			if (resetBypassTicks > 0) {
				resetBypassTicks--;
			}
			if (obj.isOnGround()) {
				if (resetBypassTicks == 0) {
					ticksInAir = 0;
				}
			} else if (EnchancementUtil.isGroundedOrAirborne(obj) && obj.getWorld().raycast(new RaycastContext(obj.getPos(), obj.getPos().add(0, -1, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, obj)).getType() == HitResult.Type.MISS) {
				ticksInAir++;
			}
		} else {
			resetBypassTicks = ticksInAir = 0;
		}
	}

	public int getTicksInAir() {
		return ticksInAir;
	}

	public void enableResetBypass() {
		resetBypassTicks = 3;
	}
}
