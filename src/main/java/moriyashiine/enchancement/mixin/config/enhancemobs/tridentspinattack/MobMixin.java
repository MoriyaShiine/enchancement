/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enhancemobs.tridentspinattack;

import moriyashiine.enchancement.common.world.entity.TridentSpinAttackUser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements TridentSpinAttackUser {
	protected MobMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public void enchancement$startAutoSpinAttack(int activationTicks, float dmg, ItemStack itemStackUsed) {
		autoSpinAttackTicks = activationTicks;
		autoSpinAttackDmg = dmg;
		autoSpinAttackItemStack = itemStackUsed;
		if (!level().isClientSide()) {
			setLivingEntityFlag(LIVING_ENTITY_FLAG_SPIN_ATTACK, true);
		}
	}
}
