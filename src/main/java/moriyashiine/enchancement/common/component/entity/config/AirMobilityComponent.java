/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.config;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class AirMobilityComponent implements CommonTickingComponent {
	private final LivingEntity obj;
	private int resetBypassTicks = 0, ticksInAir = 0;

	public AirMobilityComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		resetBypassTicks = input.getIntOr("ResetBypassTicks", 0);
		ticksInAir = input.getIntOr("TicksInAir", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putInt("ResetBypassTicks", resetBypassTicks);
		output.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void tick() {
		ItemStack stack = obj.getItemBySlot(EquipmentSlot.CHEST);
		if (stack.isEmpty()) {
			stack = obj.getItemBySlot(EquipmentSlot.BODY);
		}
		if (ModConfig.toggleablePassives && stack.getOrDefault(ModComponentTypes.TOGGLEABLE_PASSIVE, false)) {
			if (!stack.isEnchanted()) {
				stack.remove(ModComponentTypes.TOGGLEABLE_PASSIVE);
				return;
			}
			if (resetBypassTicks > 0) {
				resetBypassTicks--;
			}
			if (obj.onGround() || obj.hasEffect(MobEffects.SLOWNESS) || (obj instanceof Player player && player.getAbilities().flying)) {
				if (resetBypassTicks == 0) {
					ticksInAir = 0;
				}
			} else if (SLibUtils.isGroundedOrAirborne(obj) && SLibUtils.isSufficientlyHigh(obj, 1)) {
				ticksInAir++;
			}
		} else {
			resetBypassTicks = ticksInAir = 0;
		}
	}

	public int getTicksInAir() {
		return ticksInAir;
	}

	public void resetTicksInAir() {
		ticksInAir = 0;
	}

	public void enableResetBypass() {
		resetBypassTicks = 3;
	}
}
