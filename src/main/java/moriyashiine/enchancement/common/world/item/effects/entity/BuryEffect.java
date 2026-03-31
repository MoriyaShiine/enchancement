/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects.entity;

import com.mojang.serialization.MapCodec;
import moriyashiine.enchancement.common.component.entity.BuryEntityComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.tag.ModBlockTags;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import moriyashiine.strawberrylib.api.objects.records.ParticleVelocity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class BuryEffect implements EnchantmentEntityEffect {
	public static final BuryEffect INSTANCE = new BuryEffect();
	public static final MapCodec<BuryEffect> CODEC = MapCodec.unit(INSTANCE);

	private BuryEffect() {
	}

	@Override
	public MapCodec<BuryEffect> codec() {
		return CODEC;
	}

	@Override
	public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
		bury(serverLevel, entity, () -> {
		});
	}

	public static boolean bury(Level level, Entity entity, Runnable post) {
		if (cannotBeBuried(entity)) {
			return false;
		}
		BuryEntityComponent buryEntityComponent = ModEntityComponents.BURY_ENTITY.getNullable(entity);
		if (buryEntityComponent != null && buryEntityComponent.getBuryPos() == null) {
			for (int i = 0; i <= 1; i++) {
				BlockPos pos = entity.blockPosition().below(i);
				BlockState state = level.getBlockState(pos);
				if (state.is(ModBlockTags.BURIABLE)) {
					if (!level.isClientSide()) {
						level.playSound(null, entity, ModSoundEvents.ENTITY_GENERIC_BURY, entity.getSoundSource(), 1, 1);
						entity.gameEvent(GameEvent.ENTITY_INTERACT);
						buryEntityComponent.setBuryPos(pos);
						buryEntityComponent.sync();
					} else {
						SLibClientUtils.addParticleOptions(entity, new BlockParticleOption(ParticleTypes.BLOCK, state), 24, ParticleAnchor.BODY, ParticleVelocity.ZERO);
					}
					post.run();
					return true;
				}
			}
		}
		return false;
	}

	public static boolean cannotBeBuried(Entity entity) {
		if (entity.is(ModEntityTypeTags.CANNOT_BURY) || entity.isSpectator()) {
			return true;
		} else if (entity instanceof LivingEntity living && living.isDeadOrDying()) {
			return true;
		}
		return entity instanceof Player player && player.isCreative();
	}
}
