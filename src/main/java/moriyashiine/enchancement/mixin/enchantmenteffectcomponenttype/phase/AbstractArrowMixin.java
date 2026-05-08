/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.phase;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Entity {
	@Shadow
	public abstract byte getPierceLevel();

	@Shadow
	protected abstract void onHitEntity(EntityHitResult hitResult);

	public AbstractArrowMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Inject(method = "onHitBlock", at = @At("HEAD"), cancellable = true)
	private void enchancement$phase(BlockHitResult hitResult, CallbackInfo ci) {
		ModEntityComponents.PHASE.maybeGet(this).ifPresent(phaseComponent -> {
			if (phaseComponent.shouldPhase()) {
				int maxPhaseBlocks = phaseComponent.getMaxPhaseBlocks();
				BlockState state = level().getBlockState(hitResult.getBlockPos());
				state.onProjectileHit(level(), state, hitResult, (AbstractArrow) (Object) this);
				double distance = 0;
				Vec3 start = hitResult.getLocation(), end = start.add(getDeltaMovement().normalize().scale(1 / 16D));
				while (distance < maxPhaseBlocks + 1) {
					BlockHitResult clip = level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
					if (clip.getType() == HitResult.Type.MISS) {
						break;
					}
					distance = hitResult.getLocation().distanceTo(clip.getLocation());
					start = end;
					end = start.add(getDeltaMovement().normalize().scale(1 / 16D));
				}
				if (distance <= maxPhaseBlocks + 0.1) {
					if (!level().isClientSide()) {
						List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, new AABB(BlockPos.containing(end)), Entity::isPickable);
						for (int i = 0; i < entities.size() && i < getPierceLevel() + 1; i++) {
							onHitEntity(new EntityHitResult(entities.get(i)));
						}
						level().gameEvent(GameEvent.TELEPORT, hitResult.getLocation(), GameEvent.Context.of(this));
						SLibUtils.playSound(this, ModSoundEvents.GENERIC_TELEPORT, 0.75F, 1);
						setPos(end);
					} else {
						for (int i = 0; i < 6; i++) {
							level().addParticle(ParticleTypes.REVERSE_PORTAL, hitResult.getLocation().x() + Mth.nextDouble(random, -getBbWidth() / 2, getBbWidth() / 2), hitResult.getLocation().y() + Mth.nextDouble(random, -getBbHeight() / 2, getBbHeight() / 2), hitResult.getLocation().z() + Mth.nextDouble(random, -getBbWidth() / 2, getBbWidth() / 2), 0, 0, 0);
							level().addParticle(ParticleTypes.REVERSE_PORTAL, end.x() + Mth.nextDouble(random, -getBbWidth() / 2, getBbWidth() / 2), end.y() + Mth.nextDouble(random, -getBbHeight() / 2, getBbHeight() / 2), end.z() + Mth.nextDouble(random, -getBbWidth() / 2, getBbWidth() / 2), 0, 0, 0);
						}
					}
					phaseComponent.disable();
					ci.cancel();
				}
			}
		});
	}
}
