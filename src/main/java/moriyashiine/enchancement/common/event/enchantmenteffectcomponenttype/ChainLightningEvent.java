/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.particle.SparkParticleOption;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.AfterDamageIncludingDeathEvent;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChainLightningEvent implements AfterDamageIncludingDeathEvent {
	private boolean first = true;

	@Override
	public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
		if (!blocked && first) {
			float multiplier = 0;
			if (source.getDirectEntity() instanceof LivingEntity living) {
				multiplier = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.CHAIN_LIGHTNING, (ServerLevel) entity.level(), living.getMainHandItem(), 0);
			} else if (source.getDirectEntity() instanceof AbstractArrow arrow) {
				multiplier = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.CHAIN_LIGHTNING, (ServerLevel) entity.level(), arrow.getPickupItem(), 0);
			}
			if (multiplier != 0) {
				first = false;
				chain(new ArrayList<>(), (ServerLevel) entity.level(), entity, source, baseDamageTaken, multiplier);
				first = true;
			}
		}
	}

	private static void chain(List<LivingEntity> hitEntities, ServerLevel level, LivingEntity target, DamageSource source, float damage, float multiplier) {
		if (damage > 1 && !hitEntities.contains(target)) {
			hitEntities.add(target);
			getNearest(hitEntities, target, source.getDirectEntity()).ifPresent(nearest -> {
				target.makeSound(ModSoundEvents.GENERIC_ZAP);
				level.sendParticles(new SparkParticleOption(nearest.getEyePosition()), target.getX(), target.getEyeY(), target.getZ(), 1, 0, 0, 0, 0);
				Vec3 random = target.getEyePosition().offsetRandom(target.getRandom(), 1.5F);
				level.sendParticles(new SparkParticleOption(target.getEyePosition()), random.x(), random.y(), random.z(), 1, 0, 0, 0, 0);
				nearest.hurtServer(level, source, damage * multiplier);
				chain(hitEntities, level, nearest, source, damage * multiplier, multiplier);
			});
		}
	}

	private static Optional<LivingEntity> getNearest(List<LivingEntity> hitEntities, LivingEntity target, Entity attacker) {
		if (attacker == null) {
			return Optional.empty();
		}
		List<LivingEntity> nearby = target.level().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(3, 1, 3), foundEntity ->
						!hitEntities.contains(foundEntity) && foundEntity.distanceTo(attacker) < 8 && !(foundEntity instanceof Player) && SLibUtils.shouldHurt(attacker, foundEntity))
				.stream().sorted((e1, e2) -> Float.compare(e1.distanceTo(attacker), e2.distanceTo(attacker))).toList();
		return nearby.isEmpty() ? Optional.empty() : Optional.of(nearby.getFirst());
	}
}
