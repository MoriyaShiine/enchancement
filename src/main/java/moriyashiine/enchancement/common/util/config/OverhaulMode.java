/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.util.config;

public enum OverhaulMode {
	CHISELED(true), FREE_CHOOSE(true), NON_TREASURE(false), DISABLED(false);

	private final boolean allowsTreasure;

	OverhaulMode(boolean allowsTreasure) {
		this.allowsTreasure = allowsTreasure;
	}

	public boolean allowsTreasure() {
		return allowsTreasure;
	}
}
