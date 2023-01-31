/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.component.entity.BuryComponent;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.registry.ModTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class BuryEvent {
	public static class Unbury implements ServerLivingEntityEvents.AllowDamage {
		@Override
		public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
			BuryComponent buryComponent = ModEntityComponents.BURY.get(entity);
			if (buryComponent.getBuryPos() != null) {
				entity.teleport(entity.getX(), entity.getY() + 0.5, entity.getZ());
				buryComponent.unbury();
				return false;
			}
			return true;
		}
	}

	public static class Use implements UseEntityCallback {
		@Override
		public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
			if (!entity.getType().isIn(ModTags.EntityTypes.CANNOT_BURY) && !entity.isSpectator()) {
				if (entity instanceof LivingEntity living && living.isDead()) {
					return ActionResult.PASS;
				} else if (entity instanceof PlayerEntity targetPlayer && targetPlayer.isCreative()) {
					return ActionResult.PASS;
				}
				ItemStack stack = player.getStackInHand(hand);
				if (EnchancementUtil.hasEnchantment(ModEnchantments.BURY, stack)) {
					BuryComponent buryComponent = ModEntityComponents.BURY.getNullable(entity);
					if (buryComponent != null && buryComponent.getBuryPos() == null) {
						for (int i = 0; i <= 1; i++) {
							BlockPos pos = entity.getBlockPos().down(i);
							BlockState state = world.getBlockState(pos);
							if (state.isIn(ModTags.Blocks.BURIABLE)) {
								if (!world.isClient) {
									world.playSoundFromEntity(null, entity, ModSoundEvents.ENTITY_GENERIC_BURY, entity.getSoundCategory(), 1, 1);
									world.emitGameEvent(GameEvent.ENTITY_INTERACT, entity.getPos(), GameEvent.Emitter.of(entity, entity.getSteppingBlockState()));
									player.getItemCooldownManager().set(stack.getItem(), 200);
									stack.damage(1, player, stackUser -> stackUser.sendToolBreakStatus(hand));
									buryComponent.setBuryPos(pos);
									buryComponent.sync();
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
