/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class ExtendedWaterTimeComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private int ticksWet = 0, lastMarked = 0, delayTicks = 0;

	public ExtendedWaterTimeComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		ticksWet = input.getIntOr("TicksWet", 0);
		lastMarked = input.getIntOr("LastMarked", 0);
		delayTicks = input.getIntOr("DelayTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putInt("TicksWet", ticksWet);
		output.putInt("LastMarked", lastMarked);
		output.putInt("DelayTicks", delayTicks);
	}

	@Override
	public void tick() {
		if (ticksWet > 0) {
			if (EnchancementUtil.hasAnyEnchantmentsWith(obj, ModEnchantmentEffectComponentTypes.EXTEND_WATER_TIME)) {
				if (delayTicks > 0) {
					delayTicks--;
				} else {
					ticksWet--;
				}
			} else {
				ticksWet = 0;
			}
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (ticksWet > 0 && obj.tickCount % 10 == 0 && !obj.isInWaterOrRain()) {
			SLibUtils.playSound(obj, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER);
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (ticksWet > 0 && !obj.isInvisible() && !obj.isInWaterOrRain()) {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.getType() != EquipmentSlot.Type.HAND) {
					if (EnchantmentHelper.has(obj.getItemBySlot(slot), ModEnchantmentEffectComponentTypes.EXTEND_WATER_TIME)) {
						if (slot == EquipmentSlot.FEET) {
							SLibClientUtils.addParticles(obj, ParticleTypes.FALLING_WATER, 1, ParticleAnchor.FEET);
						} else {
							SLibClientUtils.addParticles(obj, ParticleTypes.FALLING_WATER, 1, ParticleAnchor.CHEST);
						}
					}
				}
			}
		}
	}

	public void sync() {
		ModEntityComponents.EXTENDED_WATER_TIME.sync(obj);
	}

	public int getTicksWet() {
		return ticksWet;
	}

	public int getLastMarked() {
		return lastMarked;
	}

	public void markWet(int ticks) {
		if (ticksWet < ticks) {
			ticksWet = lastMarked = ticks;
		}
		delayTicks = 3;
	}

	public void decrement(int ticks) {
		ticksWet = Math.max(0, ticksWet - ticks);
	}
}
