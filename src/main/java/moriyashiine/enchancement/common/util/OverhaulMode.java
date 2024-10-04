/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.util;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;

public enum OverhaulMode {
	ACCEPTABLE(EnchantingContext.ACCEPTABLE), PRIMARY(EnchantingContext.PRIMARY), DISABLED(null);

	public final EnchantingContext context;

	OverhaulMode(EnchantingContext context) {
		this.context = context;
	}
}
