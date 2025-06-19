/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class ProjectileTimerComponent implements CommonTickingComponent {
	private int resetTicks = 0, timesHit = 0;

	@Override
	public void readData(ReadView readView) {
		resetTicks = readView.getInt("ResetTicks", 0);
		timesHit = readView.getInt("TimesHit", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.putInt("ResetTicks", resetTicks);
		writeView.putInt("TimesHit", timesHit);
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
