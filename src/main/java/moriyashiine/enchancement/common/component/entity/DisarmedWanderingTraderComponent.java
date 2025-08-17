/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.Component;

public class DisarmedWanderingTraderComponent implements Component {
	private boolean disarmedPotion = false, disarmedMilk = false;

	@Override
	public void readData(ReadView readView) {
		disarmedMilk = readView.getBoolean("DisarmedMilk", false);
		disarmedPotion = readView.getBoolean("DisarmedPotion", false);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.putBoolean("DisarmedMilk", disarmedMilk);
		writeView.putBoolean("DisarmedPotion", disarmedPotion);
	}

	public boolean disarmedMilk() {
		return disarmedMilk;
	}

	public void disarmMilk() {
		this.disarmedMilk = true;
	}

	public boolean disarmedPotion() {
		return disarmedPotion;
	}

	public void disarmPotion() {
		this.disarmedPotion = true;
	}
}
