package moriyashiine.enchancement.mixin.phasing;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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
				BlockState state = world.getBlockState(blockHitResult.getBlockPos());
				state.onProjectileHit(world, state, blockHitResult, PersistentProjectileEntity.class.cast(this));
				if (!world.isClient) {
					Vec3d target = getPos().add(getVelocity());
					EntityHitResult entityHitResult = getEntityCollision(getPos(), target.add(getVelocity()));
					if (entityHitResult != null) {
						onEntityHit(entityHitResult);
					}
					phasingComponent.setShouldPhase(false);
					setNoGravity(false);
					((ServerWorld) world).spawnParticles(ParticleTypes.REVERSE_PORTAL, blockHitResult.getPos().getX(), blockHitResult.getPos().getY(), blockHitResult.getPos().getZ(), 6, getWidth() / 2, getHeight() / 2, getWidth() / 2, 0);
					((ServerWorld) world).spawnParticles(ParticleTypes.REVERSE_PORTAL, target.getX(), target.getY(), target.getZ(), 6, getWidth() / 2, getHeight() / 2, getWidth() / 2, 0);
					teleport(target.getX(), target.getY(), target.getZ());
				}
				ci.cancel();
			}
		});
	}
}
