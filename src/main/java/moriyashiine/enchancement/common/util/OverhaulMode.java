/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.util;

public enum OverhaulMode {
	ALL(true), NON_TREASURE(false), CHISELED(true), DISABLED(false);

	public final boolean allowsTreasure;

	OverhaulMode(boolean allowsTreasure) {
		this.allowsTreasure = allowsTreasure;
	}
}
