/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.particle.SparkParticleEffect;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChainLightningEvent implements ServerLivingEntityEvents.AllowDamage {
	private boolean first = true;

	@Override
	public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
		if (first && source.getSource() instanceof LivingEntity living) {
			float multiplier = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.CHAIN_LIGHTNING, (ServerWorld) entity.getWorld(), living.getMainHandStack(), 0);
			if (multiplier != 0) {
				first = false;
				chain(new ArrayList<>(), (ServerWorld) entity.getWorld(), entity, living, amount, multiplier);
				first = true;
			}
		}
		return true;
	}

	private static void chain(List<LivingEntity> hitEntities, ServerWorld world, LivingEntity target, LivingEntity attacker, float damage, float multiplier) {
		if (damage > 1 && !hitEntities.contains(target)) {
			hitEntities.add(target);
			getNearest(hitEntities, target, attacker).ifPresent(nearest -> {
				target.playSound(ModSoundEvents.ENTITY_GENERIC_ZAP);
				world.spawnParticles(new SparkParticleEffect(nearest.getEyePos()), target.getX(), target.getEyeY(), target.getZ(), 1, 0, 0, 0, 0);
				Vec3d random = target.getEyePos().addRandom(target.getRandom(), 1.5F);
				world.spawnParticles(new SparkParticleEffect(target.getEyePos()), random.getX(), random.getY(), random.getZ(), 1, 0, 0, 0, 0);
				nearest.damage(world, attacker instanceof PlayerEntity player ? target.getDamageSources().playerAttack(player) : target.getDamageSources().mobAttack(attacker), damage * multiplier);
				chain(hitEntities, world, nearest, attacker, damage * multiplier, multiplier);
			});
		}
	}

	private static Optional<LivingEntity> getNearest(List<LivingEntity> hitEntities, LivingEntity target, LivingEntity attacker) {
		List<LivingEntity> nearby = target.getWorld().getEntitiesByClass(LivingEntity.class, target.getBoundingBox().expand(3, 1, 3), foundEntity ->
						!hitEntities.contains(foundEntity) && foundEntity.distanceTo(attacker) < 8 && !(foundEntity instanceof PlayerEntity) && EnchancementUtil.shouldHurt(attacker, foundEntity))
				.stream().sorted((e1, e2) -> Float.compare(e1.distanceTo(attacker), e2.distanceTo(attacker))).toList();
		return nearby.isEmpty() ? Optional.empty() : Optional.of(nearby.getFirst());
	}
}
