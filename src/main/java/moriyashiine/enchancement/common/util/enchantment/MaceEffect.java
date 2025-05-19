/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.util.enchantment;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public abstract class MaceEffect {
	public static final Set<MaceEffect> EFFECTS = new HashSet<>();

	public abstract boolean canUse(Random random, ItemStack stack);

	public abstract boolean isUsing(PlayerEntity player);

	public abstract void setUsing(PlayerEntity player, boolean using);

	public abstract void use(World world, PlayerEntity player, ItemStack stack);
}
