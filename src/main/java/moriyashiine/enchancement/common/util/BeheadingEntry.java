package moriyashiine.enchancement.common.util;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class BeheadingEntry {
	public static final Map<EntityType<?>, BeheadingEntry> DROP_MAP = new HashMap<>();

	public final Item drop;
	public final float chance;

	public BeheadingEntry(Item drop, float chance) {
		this.drop = drop;
		this.chance = chance;
	}
}
