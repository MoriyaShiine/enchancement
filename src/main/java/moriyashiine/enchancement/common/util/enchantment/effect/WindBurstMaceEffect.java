/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.util.enchantment.effect;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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
		SLibUtils.playSound(player, ModSoundEvents.GENERIC_GUST, 1, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		EnchancementUtil.resetFallDistance(player);
		Vec3 delta = player.getLookAngle().normalize();
		int enchantmentLevel = EnchancementUtil.alterLevel(stack, level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.WIND_BURST));
		if (level instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(ParticleTypes.GUST, player.getX() + delta.x(), player.getEyeY() + delta.y(), player.getZ() + delta.z(), 0, 0, 0, 0, 0);
			for (Entity entity : level.getEntities(player, AABB.unitCubeFromLowerCorner(player.position().add(delta)).move(-0.5F, -0.5F, -0.5F).inflate(3), foundEntity -> SLibUtils.shouldHurt(player, foundEntity))) {
				entity.addDeltaMovement(delta.scale(1 + enchantmentLevel / 3F));
				entity.hurtMarked = true;
			}
		}
		delta = delta.scale(-0.6 - enchantmentLevel / 6F);
		delta = EnchancementUtil.modifyDeltaWithCurrent(player, delta, 0.5);
		player.setDeltaMovement(delta.x(), delta.y(), delta.z());
	}
}
