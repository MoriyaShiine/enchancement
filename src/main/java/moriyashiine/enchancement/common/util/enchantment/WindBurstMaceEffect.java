/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.util.enchantment;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.entity.WindBurstHolder;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WindBurstMaceEffect extends MaceEffect {
	@Override
	public boolean canUse(Random random, ItemStack stack) {
		return ModConfig.rebalanceEnchantments && EnchantmentHelper.getEnchantments(stack).getEnchantments().stream().anyMatch(entry -> entry.matchesKey(Enchantments.WIND_BURST));
	}

	@Override
	public boolean isUsing(PlayerEntity player) {
		return ModEntityComponents.LAUNCH_WIND_CHARGE.get(player).isUsing();
	}

	@Override
	public void setUsing(PlayerEntity player, boolean using) {
		ModEntityComponents.LAUNCH_WIND_CHARGE.get(player).setUsing(using);
	}

	@Override
	public void use(World world, PlayerEntity player, ItemStack stack) {
		SLibUtils.playSound(player, SoundEvents.ENTITY_WIND_CHARGE_THROW, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!world.isClient) {
			WindChargeEntity windChargeEntity = new WindChargeEntity(player, world, player.getPos().getX(), player.getEyePos().getY(), player.getPos().getZ());
			windChargeEntity.setVelocity(player, player.getPitch(), player.getYaw(), 0, 1.5F, 1);
			((WindBurstHolder) windChargeEntity).enchancement$setFromWindBurst(true);
			world.spawnEntity(windChargeEntity);
		}
	}
}
