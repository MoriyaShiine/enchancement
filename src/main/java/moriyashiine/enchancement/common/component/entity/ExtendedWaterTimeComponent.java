/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundEvents;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class ExtendedWaterTimeComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private int ticksWet = 0;

	public ExtendedWaterTimeComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		ticksWet = tag.getInt("TicksWet");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putInt("TicksWet", ticksWet);
	}

	@Override
	public void tick() {
		if (ticksWet > 0) {
			if (EnchancementUtil.hasAnyEnchantmentsWith(obj, ModEnchantmentEffectComponentTypes.EXTEND_WATER_TIME)) {
				ticksWet--;
			} else {
				ticksWet = 0;
			}
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (ticksWet > 0 && obj.age % 10 == 0 && !obj.isWet()) {
			SLibUtils.playSound(obj, SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_WATER);
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (ticksWet > 0 && !obj.isInvisible() && !obj.isWet()) {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.isArmorSlot()) {
					if (EnchantmentHelper.hasAnyEnchantmentsWith(obj.getEquippedStack(slot), ModEnchantmentEffectComponentTypes.EXTEND_WATER_TIME)) {
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

	public void markWet(int ticks) {
		if (ticksWet < ticks) {
			ticksWet = ticks;
		}
	}

	public void decrement(int ticks) {
		ticksWet = Math.max(0, ticksWet - ticks);
	}
}
