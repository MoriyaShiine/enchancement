/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.entity;

import net.minecraft.world.item.ItemStack;

public interface TridentSpinAttackUser {
	void enchancement$startAutoSpinAttack(int activationTicks, float dmg, ItemStack itemStackUsed);
}
