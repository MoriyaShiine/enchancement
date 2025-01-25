/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
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
			obj.getWorld().playSound(null, obj.getBlockPos(), SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_WATER, obj.getSoundCategory(), 1, 1);
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (ticksWet > 0 && !obj.isInvisible() && !obj.isWet() && EnchancementClientUtil.shouldAddParticles(obj)) {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.isArmorSlot()) {
					if (EnchantmentHelper.hasAnyEnchantmentsWith(obj.getEquippedStack(slot), ModEnchantmentEffectComponentTypes.EXTEND_WATER_TIME)) {
						if (slot == EquipmentSlot.FEET) {
							obj.getWorld().addParticle(ParticleTypes.FALLING_WATER, obj.getParticleX(1), obj.getY() + obj.getHeight() * 0.15, obj.getParticleZ(1), 0, 0, 0);
						} else {
							obj.getWorld().addParticle(ParticleTypes.FALLING_WATER, obj.getParticleX(1), obj.getY() + obj.getHeight() * MathHelper.nextDouble(obj.getRandom(), 0.4, 0.8), obj.getParticleZ(1), 0, 0, 0);
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
