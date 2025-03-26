/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class ProjectileTimerComponent implements CommonTickingComponent {
	private int resetTicks = 0, timesHit = 0;

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		resetTicks = tag.getInt("ResetTicks", 0);
		timesHit = tag.getInt("TimesHit", 0);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putInt("ResetTicks", resetTicks);
		tag.putInt("TimesHit", timesHit);
	}

	@Override
	public void tick() {
		if (resetTicks > 0) {
			resetTicks--;
			if (resetTicks == 0) {
				timesHit = 0;
			}
		}
	}

	public int getTimesHit() {
		return timesHit;
	}

	public void incrementTimesHit() {
		timesHit++;
	}

	public void markAsHit() {
		resetTicks = 20;
	}
}
