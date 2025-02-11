/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect.entity;

import com.mojang.serialization.MapCodec;
import moriyashiine.enchancement.common.component.entity.BuryEntityComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.tag.ModBlockTags;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class BuryEffect implements EnchantmentEntityEffect {
	public static final BuryEffect INSTANCE = new BuryEffect();
	public static final MapCodec<BuryEffect> CODEC = MapCodec.unit(INSTANCE);

	private BuryEffect() {
	}

	@Override
	public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
		bury(world, user, () -> {
		});
	}

	@Override
	public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
		return CODEC;
	}

	public static boolean bury(World world, Entity entity, Runnable post) {
		if (entity.getType().isIn(ModEntityTypeTags.CANNOT_BURY) || entity.isSpectator()) {
			return false;
		} else if (entity instanceof LivingEntity living && living.isDead()) {
			return false;
		} else if (entity instanceof PlayerEntity targetPlayer && targetPlayer.isCreative()) {
			return false;
		}
		BuryEntityComponent buryEntityComponent = ModEntityComponents.BURY_ENTITY.getNullable(entity);
		if (buryEntityComponent != null && buryEntityComponent.getBuryPos() == null) {
			for (int i = 0; i <= 1; i++) {
				BlockPos pos = entity.getBlockPos().down(i);
				BlockState state = world.getBlockState(pos);
				if (state.isIn(ModBlockTags.BURIABLE)) {
					if (!world.isClient) {
						world.playSoundFromEntity(null, entity, ModSoundEvents.ENTITY_GENERIC_BURY, entity.getSoundCategory(), 1, 1);
						world.emitGameEvent(GameEvent.ENTITY_INTERACT, entity.getPos(), GameEvent.Emitter.of(entity, entity.getSteppingBlockState()));
						buryEntityComponent.setBuryPos(pos);
						buryEntityComponent.sync();
					} else {
						BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, state);
						for (int j = 0; j < 24; j++) {
							world.addParticle(particle, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
						}
					}
					post.run();
					return true;
				}
			}
		}
		return false;
	}
}
