/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.util.enchantment.effect;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

public abstract class MaceEffect {
	public static final Set<MaceEffect> EFFECTS = new HashSet<>();

	public abstract boolean canUse(RandomSource random, ItemStack stack);

	public abstract boolean isUsing(Player player);

	public abstract void setUsing(Player player, boolean using);

	public abstract void use(Level level, Player player, ItemStack stack);
}
