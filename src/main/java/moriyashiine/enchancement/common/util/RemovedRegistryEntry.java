/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public record RemovedRegistryEntry(Enchantment enchantment, Identifier identifier, int rawId) {
	public static final Set<RemovedRegistryEntry> REMOVED_ENTRIES = new HashSet<>();

	@Nullable
	public static RemovedRegistryEntry getFromId(Identifier identifier) {
		for (RemovedRegistryEntry entry : REMOVED_ENTRIES) {
			if (entry.identifier.equals(identifier)) {
				return entry;
			}
		}
		return null;
	}

	@Nullable
	public static RemovedRegistryEntry getFromEnchantment(Enchantment enchantment) {
		for (RemovedRegistryEntry entry : REMOVED_ENTRIES) {
			if (entry.enchantment.equals(enchantment)) {
				return entry;
			}
		}
		return null;
	}
}
