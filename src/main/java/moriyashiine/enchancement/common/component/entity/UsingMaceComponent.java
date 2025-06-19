/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class UsingMaceComponent implements AutoSyncedComponent {
	private boolean using = false;

	@Override
	public void readData(ReadView readView) {
		using = readView.getBoolean("Using", false);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.putBoolean("Using", using);
	}

	public void setUsing(boolean using) {
		this.using = using;
	}

	public boolean isUsing() {
		return using;
	}
}
