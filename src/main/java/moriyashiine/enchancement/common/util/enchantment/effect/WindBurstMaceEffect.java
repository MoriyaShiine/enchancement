/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.util.enchantment.effect;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.world.entity.WindBurstHolder;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class WindBurstMaceEffect extends MaceEffect {
	@Override
	public boolean canUse(RandomSource random, ItemStack stack) {
		return ModConfig.rebalanceEnchantments && stack.getEnchantments().keySet().stream().anyMatch(entry -> entry.is(Enchantments.WIND_BURST));
	}

	@Override
	public boolean isUsing(Player player) {
		return ModEntityComponents.LAUNCH_WIND_CHARGE.get(player).isUsing();
	}

	@Override
	public void setUsing(Player player, boolean using) {
		ModEntityComponents.LAUNCH_WIND_CHARGE.get(player).setUsing(using);
	}

	@Override
	public void use(Level level, Player player, ItemStack stack) {
		SLibUtils.playSound(player, SoundEvents.WIND_CHARGE_THROW, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!level.isClientSide()) {
			WindCharge windCharge = new WindCharge(player, level, player.position().x(), player.getEyePosition().y(), player.position().z());
			windCharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 1);
			((WindBurstHolder) windCharge).enchancement$setFromWindBurst(true);
			level.addFreshEntity(windCharge);
		}
	}
}
