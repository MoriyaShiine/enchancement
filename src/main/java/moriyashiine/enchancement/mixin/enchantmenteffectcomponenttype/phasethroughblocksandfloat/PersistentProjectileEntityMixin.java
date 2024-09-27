/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.phasethroughblocksandfloat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.component.entity.PhaseThroughBlocksAndFloatComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends Entity {
	@Shadow
	public abstract byte getPierceLevel();

	@Shadow
	protected abstract void onEntityHit(EntityHitResult entityHitResult);

	public PersistentProjectileEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "onBlockHit", at = @At("HEAD"), cancellable = true)
	private void enchancement$phaseThroughBlocksAndFloat(BlockHitResult blockHitResult, CallbackInfo ci) {
		ModEntityComponents.PHASE_THROUGH_BLOCKS_AND_FLOAT.maybeGet(this).ifPresent(phaseThroughBlocksAndFloatComponent -> {
			if (phaseThroughBlocksAndFloatComponent.shouldPhase()) {
				int maxPhaseBlocks = phaseThroughBlocksAndFloatComponent.getMaxPhaseBlocks();
				BlockState state = getWorld().getBlockState(blockHitResult.getBlockPos());
				state.onProjectileHit(getWorld(), state, blockHitResult, (PersistentProjectileEntity) (Object) this);
				double distance = 0;
				Vec3d start = blockHitResult.getPos(), end = start.add(getVelocity().normalize().multiply(1 / 16D));
				while (distance < maxPhaseBlocks + 1) {
					BlockHitResult hitResult = getWorld().raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
					if (hitResult.getType() == HitResult.Type.MISS) {
						break;
					}
					distance = blockHitResult.getPos().distanceTo(hitResult.getPos());
					start = end;
					end = start.add(getVelocity().normalize().multiply(1 / 16D));
				}
				if (distance <= maxPhaseBlocks + 0.1) {
					if (!getWorld().isClient) {
						List<LivingEntity> entities = getWorld().getEntitiesByClass(LivingEntity.class, new Box(BlockPos.ofFloored(end)), Entity::canHit);
						for (int i = 0; i < entities.size() && i < getPierceLevel() + 1; i++) {
							onEntityHit(new EntityHitResult(entities.get(i)));
						}
						getWorld().emitGameEvent(GameEvent.TELEPORT, blockHitResult.getPos(), GameEvent.Emitter.of(this));
						getWorld().playSound(null, getBlockPos(), ModSoundEvents.ENTITY_GENERIC_TELEPORT, getSoundCategory(), 0.75F, 1);
						setPosition(end);
					} else {
						for (int i = 0; i < 6; i++) {
							getWorld().addParticle(ParticleTypes.REVERSE_PORTAL, blockHitResult.getPos().getX() + MathHelper.nextDouble(random, -getWidth() / 2, getWidth() / 2), blockHitResult.getPos().getY() + MathHelper.nextDouble(random, -getHeight() / 2, getHeight() / 2), blockHitResult.getPos().getZ() + MathHelper.nextDouble(random, -getWidth() / 2, getWidth() / 2), 0, 0, 0);
							getWorld().addParticle(ParticleTypes.REVERSE_PORTAL, end.getX() + MathHelper.nextDouble(random, -getWidth() / 2, getWidth() / 2), end.getY() + MathHelper.nextDouble(random, -getHeight() / 2, getHeight() / 2), end.getZ() + MathHelper.nextDouble(random, -getWidth() / 2, getWidth() / 2), 0, 0, 0);
						}
					}
					phaseThroughBlocksAndFloatComponent.disable();
					ci.cancel();
				}
			}
		});
	}

	@WrapOperation(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;length()D"))
	private double enchancement$phaseThroughBlocksAndFloat(Vec3d instance, Operation<Double> original) {
		PhaseThroughBlocksAndFloatComponent phaseThroughBlocksAndFloatComponent = ModEntityComponents.PHASE_THROUGH_BLOCKS_AND_FLOAT.getNullable(this);
		if (phaseThroughBlocksAndFloatComponent != null && phaseThroughBlocksAndFloatComponent.getVelocityLength() != -1) {
			return phaseThroughBlocksAndFloatComponent.getVelocityLength();
		}
		return original.call(instance);
	}
}
