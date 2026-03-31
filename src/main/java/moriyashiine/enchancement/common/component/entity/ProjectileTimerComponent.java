/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class ProjectileTimerComponent implements CommonTickingComponent {
	private int resetTicks = 0, timesHit = 0;

	@Override
	public void readData(ValueInput input) {
		resetTicks = input.getIntOr("ResetTicks", 0);
		timesHit = input.getIntOr("TimesHit", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putInt("ResetTicks", resetTicks);
		output.putInt("TimesHit", timesHit);
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
