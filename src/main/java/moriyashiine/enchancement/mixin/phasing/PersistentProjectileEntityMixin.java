/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.phasing;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends Entity {
	@Shadow
	protected abstract @Nullable EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition);

	@Shadow
	protected abstract void onEntityHit(EntityHitResult entityHitResult);

	public PersistentProjectileEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "onBlockHit", at = @At("HEAD"), cancellable = true)
	private void enchancement$phasing(BlockHitResult blockHitResult, CallbackInfo ci) {
		ModEntityComponents.PHASHING.maybeGet(this).ifPresent(phasingComponent -> {
			if (phasingComponent.shouldPhase()) {
				BlockState state = getWorld().getBlockState(blockHitResult.getBlockPos());
				state.onProjectileHit(getWorld(), state, blockHitResult, PersistentProjectileEntity.class.cast(this));
				double distance = 0;
				Vec3d start = getPos(), end = start.add(getVelocity().multiply(1 / 8F).normalize());
				while (distance < 4) {
					BlockHitResult hitResult = getWorld().raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
					if (hitResult.getType() == HitResult.Type.MISS) {
						break;
					}
					distance = getPos().distanceTo(hitResult.getPos());
					start = end;
					end = start.add(getVelocity().multiply(1 / 8F).normalize());
				}
				if (distance <= 3.1) {
					if (!getWorld().isClient) {
						Vec3d target = getPos().add(getVelocity());
						EntityHitResult entityHitResult = getEntityCollision(getPos(), target.add(getVelocity()));
						if (entityHitResult != null) {
							onEntityHit(entityHitResult);
						}
						setNoGravity(false);
						getWorld().emitGameEvent(GameEvent.TELEPORT, getPos(), GameEvent.Emitter.of(this));
						teleport(end.getX(), end.getY(), end.getZ());
					} else {
						for (int i = 0; i < 6; i++) {
							getWorld().addParticle(ParticleTypes.REVERSE_PORTAL, blockHitResult.getPos().getX() + MathHelper.nextDouble(random, -getWidth() / 2, getWidth() / 2), blockHitResult.getPos().getY() + MathHelper.nextDouble(random, -getHeight() / 2, getHeight() / 2), blockHitResult.getPos().getZ() + MathHelper.nextDouble(random, -getWidth() / 2, getWidth() / 2), 0, 0, 0);
							getWorld().addParticle(ParticleTypes.REVERSE_PORTAL, end.getX() + MathHelper.nextDouble(random, -getWidth() / 2, getWidth() / 2), end.getY() + MathHelper.nextDouble(random, -getHeight() / 2, getHeight() / 2), end.getZ() + MathHelper.nextDouble(random, -getWidth() / 2, getWidth() / 2), 0, 0, 0);
						}
					}
					phasingComponent.setShouldPhase(false);
					ci.cancel();
				}
			}
		});
	}
}
