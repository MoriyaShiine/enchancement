package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class ImpactComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private int impactTicks = 0;

	private boolean hasImpact = false, wasSneaking = false;

	public ImpactComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		impactTicks = tag.getInt("ImpactTicks");
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		tag.putInt("ImpactTicks", impactTicks);
	}

	@Override
	public void tick() {
		hasImpact = EnchantmentHelper.getEquipmentLevel(ModEnchantments.IMPACT, obj) > 0;
		if (hasImpact) {
			boolean sneaking = obj.isSneaking();
			if (obj.isOnGround()) {
				if (impactTicks > 0) {
					obj.playSound(ModSoundEvents.ENTITY_GENERIC_IMPACT, 1, 1);
				}
				impactTicks = 0;
			} else if (impactTicks > 0 || (sneaking && !wasSneaking && !obj.getAbilities().flying)) {
				impactTicks++;
				if (impactTicks < 15) {
					obj.setVelocity(Vec3d.ZERO);
				} else {
					obj.setVelocity(obj.getVelocity().getX(), -1.5, obj.getVelocity().getZ());
					obj.fallDistance = 0;
				}
			}
			wasSneaking = sneaking;
		} else {
			impactTicks = 0;
			wasSneaking = false;
		}
	}

	@Override
	public void serverTick() {
		if (hasImpact && obj.isOnGround() && impactTicks > 0) {
			obj.getWorld().getOtherEntities(obj, new Box(obj.getBlockPos()).expand(5, 1, 5), foundEntity -> foundEntity.isAlive() && foundEntity.distanceTo(obj) < 5).forEach(entity -> {
				if (entity instanceof LivingEntity living && EnchancementUtil.shouldHurt(obj, living)) {
					living.damage(DamageSource.player(obj), 2);
					living.takeKnockback(0.75, obj.getX() - living.getX(), obj.getZ() - living.getZ());
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
					BlockState state = obj.world.getBlockState(mutable.set(x, Math.round(obj.getY()), z));
					if (!state.isAir()) {
						obj.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), x, mutable.getY(), z, 0, 0, 0);
					}
				}
			}
		}
		tick();
	}

	public boolean shouldDamage() {
		return impactTicks >= 15;
	}

	public boolean shouldForceSneak() {
		return impactTicks > 0;
	}
}
