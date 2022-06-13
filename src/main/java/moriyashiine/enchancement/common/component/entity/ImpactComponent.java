/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.NotNull;

public class ImpactComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldRefreshImpact = false;
	private int impactCooldown = 60, impactTicks = 0;

	private boolean hasImpact = false, wasSneaking = false;

	public ImpactComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		shouldRefreshImpact = tag.getBoolean("ShouldRefreshImpact");
		impactCooldown = tag.getInt("ImpactCooldown");
		impactTicks = tag.getInt("ImpactTicks");
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		tag.putBoolean("ShouldRefreshImpact", shouldRefreshImpact);
		tag.putInt("ImpactCooldown", impactCooldown);
		tag.putInt("ImpactTicks", impactTicks);
	}

	@Override
	public void tick() {
		hasImpact = EnchancementUtil.hasEnchantment(ModEnchantments.IMPACT, obj);
		if (hasImpact) {
			boolean sneaking = obj.isSneaking();
			if (shouldRefreshImpact && impactCooldown > 0) {
				impactCooldown--;
			}
			if (obj.isOnGround()) {
				shouldRefreshImpact = true;
				if (impactTicks > 0) {
					obj.playSound(ModSoundEvents.ENTITY_GENERIC_IMPACT, 1, 1);
				}
				impactTicks = 0;
			} else if (impactTicks > 0 || (impactCooldown == 0 && sneaking && !wasSneaking && EnchancementUtil.isGroundedOrJumping(obj) && obj.world.raycast(new RaycastContext(obj.getPos(), obj.getPos().add(0, -2, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, obj)).getType() == HitResult.Type.MISS)) {
				shouldRefreshImpact = false;
				impactCooldown = 60;
				impactTicks++;
				obj.setVelocity(obj.getVelocity().getX(), -1.5, obj.getVelocity().getZ());
				obj.fallDistance = 0;
			}
			wasSneaking = sneaking;
		} else {
			impactCooldown = 60;
			impactTicks = 0;
			wasSneaking = false;
		}
	}

	@Override
	public void serverTick() {
		if (hasImpact && obj.isOnGround() && impactTicks > 0) {
			obj.getWorld().getOtherEntities(obj, new Box(obj.getBlockPos()).expand(5, 1, 5), foundEntity -> foundEntity.isAlive() && foundEntity.distanceTo(obj) < 5).forEach(entity -> {
				if (entity instanceof LivingEntity living && EnchancementUtil.shouldHurt(obj, living)) {
					float delta = MathHelper.clamp(impactTicks / 60F, 0, 1);
					living.damage(DamageSource.player(obj), MathHelper.lerp(delta, 4, 40));
					living.takeKnockback(MathHelper.lerp(delta, 1.5, 10), obj.getX() - living.getX(), obj.getZ() - living.getZ());
				}
			});
		}
		tick();
	}

	@Override
	public void clientTick() {
		if (hasImpact && obj.isOnGround() && impactTicks > 0) {
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			for (int i = 0; i < 360; i += 15) {
				for (int j = 1; j < 10; j++) {
					double x = obj.getX() + MathHelper.sin(i) * j / 2, z = obj.getZ() + MathHelper.cos(i) * j / 2;
					BlockState state = obj.world.getBlockState(mutable.set(x, Math.round(obj.getY() - 1), z));
					if (!state.getMaterial().isReplaceable() && obj.world.getBlockState(mutable.move(Direction.UP)).getMaterial().isReplaceable()) {
						obj.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), x, mutable.getY(), z, 0, 0, 0);
					}
				}
			}
		}
		tick();
	}

	public boolean isFalling() {
		return impactTicks > 0;
	}
}
