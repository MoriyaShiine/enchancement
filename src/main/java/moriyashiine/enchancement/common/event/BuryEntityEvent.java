/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.component.entity.BuryEntityComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.tag.ModBlockTags;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class BuryEntityEvent {
	public static class Unbury implements ServerLivingEntityEvents.AllowDamage {
		@Override
		public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
			BuryEntityComponent buryEntityComponent = ModEntityComponents.BURY_ENTITY.get(entity);
			if (buryEntityComponent.getBuryPos() != null) {
				entity.setPosition(entity.getX(), entity.getY() + 0.5, entity.getZ());
				buryEntityComponent.unbury();
				return false;
			}
			return true;
		}
	}

	public static class Use implements UseEntityCallback {
		@Override
		public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
			if (!entity.getType().isIn(ModEntityTypeTags.CANNOT_BURY) && !entity.isSpectator()) {
				if (entity instanceof LivingEntity living && living.isDead()) {
					return ActionResult.PASS;
				} else if (entity instanceof PlayerEntity targetPlayer && targetPlayer.isCreative()) {
					return ActionResult.PASS;
				}
				ItemStack stack = player.getStackInHand(hand);
				if (!player.getItemCooldownManager().isCoolingDown(stack.getItem()) && EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.BURY_ENTITY)) {
					BuryEntityComponent buryEntityComponent = ModEntityComponents.BURY_ENTITY.getNullable(entity);
					if (buryEntityComponent != null && buryEntityComponent.getBuryPos() == null) {
						for (int i = 0; i <= 1; i++) {
							BlockPos pos = entity.getBlockPos().down(i);
							BlockState state = world.getBlockState(pos);

							if (state.isIn(ModBlockTags.BURIABLE)) {
								if (!world.isClient) {
									int cooldown = MathHelper.floor(EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.BURY_ENTITY, (ServerWorld) world, stack, 0) * 20);
									if (cooldown > 0) {
										player.getItemCooldownManager().set(stack.getItem(), cooldown);
									}
									world.playSoundFromEntity(null, entity, ModSoundEvents.ENTITY_GENERIC_BURY, entity.getSoundCategory(), 1, 1);
									world.emitGameEvent(GameEvent.ENTITY_INTERACT, entity.getPos(), GameEvent.Emitter.of(entity, entity.getSteppingBlockState()));
									player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
									stack.damage(1, player, LivingEntity.getSlotForHand(hand));
									buryEntityComponent.setBuryPos(pos);
									buryEntityComponent.sync();
								} else {
									BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, state);
									for (int j = 0; j < 24; j++) {
										world.addParticle(particle, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
									}
								}
								return ActionResult.success(world.isClient);
							}
						}
					}
				}
			}
			return ActionResult.PASS;
		}
	}
}
