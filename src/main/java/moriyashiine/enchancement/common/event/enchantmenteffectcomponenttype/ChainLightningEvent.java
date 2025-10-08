/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.particle.SparkParticleEffect;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.AfterDamageIncludingDeathEvent;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChainLightningEvent implements AfterDamageIncludingDeathEvent {
	private boolean first = true;

	@Override
	public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
		if (!blocked && first) {
			float multiplier = 0;
			if (source.getSource() instanceof LivingEntity living) {
				multiplier = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.CHAIN_LIGHTNING, (ServerWorld) entity.getEntityWorld(), living.getMainHandStack(), 0);
			} else if (source.getSource() instanceof PersistentProjectileEntity projectile) {
				multiplier = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.CHAIN_LIGHTNING, (ServerWorld) entity.getEntityWorld(), projectile.asItemStack(), 0);
			}
			if (multiplier != 0) {
				first = false;
				chain(new ArrayList<>(), (ServerWorld) entity.getEntityWorld(), entity, source, baseDamageTaken, multiplier);
				first = true;
			}
		}
	}

	private static void chain(List<LivingEntity> hitEntities, ServerWorld world, LivingEntity target, DamageSource source, float damage, float multiplier) {
		if (damage > 1 && !hitEntities.contains(target)) {
			hitEntities.add(target);
			getNearest(hitEntities, target, source.getSource()).ifPresent(nearest -> {
				target.playSound(ModSoundEvents.ENTITY_GENERIC_ZAP);
				world.spawnParticles(new SparkParticleEffect(nearest.getEyePos()), target.getX(), target.getEyeY(), target.getZ(), 1, 0, 0, 0, 0);
				Vec3d random = target.getEyePos().addRandom(target.getRandom(), 1.5F);
				world.spawnParticles(new SparkParticleEffect(target.getEyePos()), random.getX(), random.getY(), random.getZ(), 1, 0, 0, 0, 0);
				nearest.damage(world, source, damage * multiplier);
				chain(hitEntities, world, nearest, source, damage * multiplier, multiplier);
			});
		}
	}

	private static Optional<LivingEntity> getNearest(List<LivingEntity> hitEntities, LivingEntity target, Entity attacker) {
		if (attacker == null) {
			return Optional.empty();
		}
		List<LivingEntity> nearby = target.getEntityWorld().getEntitiesByClass(LivingEntity.class, target.getBoundingBox().expand(3, 1, 3), foundEntity ->
						!hitEntities.contains(foundEntity) && foundEntity.distanceTo(attacker) < 8 && !(foundEntity instanceof PlayerEntity) && SLibUtils.shouldHurt(attacker, foundEntity))
				.stream().sorted((e1, e2) -> Float.compare(e1.distanceTo(attacker), e2.distanceTo(attacker))).toList();
		return nearby.isEmpty() ? Optional.empty() : Optional.of(nearby.getFirst());
	}
}
