/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.util.enchantment;

import moriyashiine.enchancement.client.payload.UseEruptionPayload;
import moriyashiine.enchancement.common.enchantment.effect.EruptionEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class EruptionMaceEffect extends MaceEffect {
	@Override
	public boolean canUse(Random random, ItemStack stack) {
		return EruptionEffect.getJumpStrength(random, stack) != 0;
	}

	@Override
	public boolean isUsing(PlayerEntity player) {
		return ModEntityComponents.ERUPTION.get(player).isUsing();
	}

	@Override
	public void setUsing(PlayerEntity player, boolean using) {
		ModEntityComponents.ERUPTION.get(player).setUsing(using);
	}

	@Override
	public void use(World world, PlayerEntity player, ItemStack stack) {
		EruptionMaceEffect.useCommon(player);
		if (world.isClient()) {
			EruptionMaceEffect.useClient(player);
		} else {
			EruptionMaceEffect.useServer(player);
		}
	}

	public static void useCommon(PlayerEntity player) {
		SLibUtils.playSound(player, ModSoundEvents.ENTITY_GENERIC_ERUPT, 1, MathHelper.nextFloat(player.getRandom(), 0.8F, 1.2F));
		player.setVelocity(player.getVelocity().getX(), EruptionEffect.getJumpStrength(player.getRandom(), player.getMainHandStack()), player.getVelocity().getZ());
		player.emitGameEvent(GameEvent.ENTITY_ACTION);
	}

	public static void useClient(PlayerEntity player) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		double y = Math.round(player.getY() - 1);
		for (int i = 0; i < 360; i += 15) {
			for (int j = 1; j < 4; j++) {
				double x = player.getX() + MathHelper.sin(i) * j / 2, z = player.getZ() + MathHelper.cos(i) * j / 2;
				BlockState state = player.getEntityWorld().getBlockState(mutable.set(x, y, z));
				if (!state.isReplaceable() && player.getEntityWorld().getBlockState(mutable.move(Direction.UP)).isReplaceable()) {
					BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, state);
					for (int k = 0; k < 2; k++) {
						player.getEntityWorld().addParticleClient(particle, x, mutable.getY() + 0.5, z, 0, 0.5, 0);
						player.getEntityWorld().addParticleClient(ParticleTypes.LAVA, x, mutable.getY() + 0.5, z, 0, 2, 0);
					}
				}
			}
		}
	}

	public static void useServer(PlayerEntity player) {
		ServerWorld serverWorld = (ServerWorld) player.getEntityWorld();
		PlayerLookup.tracking(player).forEach(foundPlayer -> UseEruptionPayload.send(foundPlayer, player));
		float base = (float) player.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
		float fireDuration = EruptionEffect.getFireDuration(player.getRandom(), player.getMainHandStack());
		getNearby(player).forEach(entity -> {
			DamageSource source = entity.getDamageSources().playerAttack(player);
			float damage = EnchantmentHelper.getDamage(serverWorld, player.getMainHandStack(), entity, source, base)
					+ player.getMainHandStack().getItem().getBonusAttackDamage(entity, base, source);
			entity.setOnFireFor(fireDuration);
			if (entity.damage(serverWorld, source, damage)) {
				entity.takeKnockback(1, player.getX() - entity.getX(), player.getZ() - entity.getZ());
			}
		});
	}

	private static List<LivingEntity> getNearby(LivingEntity living) {
		return living.getEntityWorld().getEntitiesByClass(LivingEntity.class, new Box(living.getBlockPos()).expand(2), foundEntity ->
				foundEntity.isAlive() && foundEntity.distanceTo(living) < 10 && SLibUtils.shouldHurt(living, foundEntity) && SLibUtils.canSee(living, foundEntity, 2));
	}
}
