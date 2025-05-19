/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.util.enchantment;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public record BeheadingEntry(Item drop, float chance) {
	public static final Map<EntityType<?>, BeheadingEntry> DROP_MAP = new HashMap<>();
}
