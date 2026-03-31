/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.util.enchantment.effect;

import moriyashiine.enchancement.client.payload.UseEruptionPayload;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.world.item.effects.EruptionEffect;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EruptionMaceEffect extends MaceEffect {
	@Override
	public boolean canUse(RandomSource random, ItemStack stack) {
		return EruptionEffect.getJumpStrength(random, stack) != 0;
	}

	@Override
	public boolean isUsing(Player player) {
		return ModEntityComponents.ERUPTION.get(player).isUsing();
	}

	@Override
	public void setUsing(Player player, boolean using) {
		ModEntityComponents.ERUPTION.get(player).setUsing(using);
	}

	@Override
	public void use(Level level, Player player, ItemStack stack) {
		EruptionMaceEffect.useCommon(player);
		if (level.isClientSide()) {
			EruptionMaceEffect.useClient(player);
		} else {
			EruptionMaceEffect.useServer(player);
		}
	}

	public static void useCommon(Player player) {
		SLibUtils.playSound(player, ModSoundEvents.ENTITY_GENERIC_ERUPT, 1, Mth.nextFloat(player.getRandom(), 0.8F, 1.2F));
		player.setDeltaMovement(player.getDeltaMovement().x(), EruptionEffect.getJumpStrength(player.getRandom(), player.getMainHandItem()), player.getDeltaMovement().z());
		player.gameEvent(GameEvent.ENTITY_ACTION);
	}

	public static void useClient(Player player) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		double y = Math.round(player.getY() - 1);
		for (int i = 0; i < 360; i += 15) {
			for (int j = 1; j < 4; j++) {
				double x = player.getX() + Mth.sin(i) * j / 2, z = player.getZ() + Mth.cos(i) * j / 2;
				BlockState state = player.level().getBlockState(mutable.set(x, y, z));
				if (!state.canBeReplaced() && player.level().getBlockState(mutable.move(Direction.UP)).canBeReplaced()) {
					BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, state);
					for (int k = 0; k < 2; k++) {
						player.level().addParticle(particle, x, mutable.getY() + 0.5, z, 0, 0.5, 0);
						player.level().addParticle(ParticleTypes.LAVA, x, mutable.getY() + 0.5, z, 0, 2, 0);
					}
				}
			}
		}
	}

	public static void useServer(Player player) {
		ServerLevel level = (ServerLevel) player.level();
		PlayerLookup.tracking(player).forEach(receiver -> UseEruptionPayload.send(receiver, player));
		float base = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
		float fireDuration = EruptionEffect.getFireDuration(player.getRandom(), player.getMainHandItem());
		getNearby(player).forEach(entity -> {
			DamageSource source = entity.damageSources().playerAttack(player);
			float damage = EnchantmentHelper.modifyDamage(level, player.getMainHandItem(), entity, source, base) + player.getMainHandItem().getItem().getAttackDamageBonus(entity, base, source);
			entity.igniteForSeconds(fireDuration);
			if (entity.hurtServer(level, source, damage)) {
				entity.knockback(1, player.getX() - entity.getX(), player.getZ() - entity.getZ());
			}
		});
	}

	private static List<LivingEntity> getNearby(LivingEntity living) {
		return living.level().getEntitiesOfClass(LivingEntity.class, new AABB(living.blockPosition()).inflate(2), foundEntity ->
				foundEntity.isAlive() && foundEntity.distanceTo(living) < 10 && SLibUtils.shouldHurt(living, foundEntity) && SLibUtils.hasLineOfSight(living, foundEntity, 2));
	}
}
