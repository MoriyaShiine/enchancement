/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class AirMobilityComponent implements CommonTickingComponent {
	private final LivingEntity obj;
	private int resetBypassTicks = 0, ticksInAir = 0;

	public AirMobilityComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		resetBypassTicks = tag.getInt("ResetBypassTicks");
		ticksInAir = tag.getInt("TicksInAir");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putInt("ResetBypassTicks", resetBypassTicks);
		tag.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void tick() {
		ItemStack stack = obj.getEquippedStack(EquipmentSlot.CHEST);
		if (ModConfig.enchantedChestplatesIncreaseAirMobility && stack.getOrDefault(ModComponentTypes.TOGGLEABLE_PASSIVE, false)) {
			if (!stack.hasEnchantments()) {
				stack.remove(ModComponentTypes.TOGGLEABLE_PASSIVE);
				return;
			}
			if (resetBypassTicks > 0) {
				resetBypassTicks--;
			}
			if (obj.isOnGround()) {
				if (resetBypassTicks == 0) {
					ticksInAir = 0;
				}
			} else if (EnchancementUtil.isGroundedOrAirborne(obj) && EnchancementUtil.isSufficientlyHigh(obj, 1)) {
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
