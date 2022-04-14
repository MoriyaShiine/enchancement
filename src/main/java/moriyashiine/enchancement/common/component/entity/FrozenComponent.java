package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.registry.ModTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class FrozenComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity obj;
	private Entity lastFreezingAttacker = null;
	private boolean frozen = false;
	private int ticksFrozen = 0;
	private EntityPose forcedPose = EntityPose.STANDING;
	private float forcedHeadYaw, forcedBodyYaw, forcedPitch, forcedLimbDistance, forcedLimbAngle;

	public FrozenComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		if (tag.contains("Frozen")) {
			frozen = tag.getBoolean("Frozen");
			ticksFrozen = tag.getInt("TicksFrozen");
			forcedPose = EntityPose.valueOf(tag.getString("ForcedPose"));
			forcedHeadYaw = tag.getFloat("ForcedHeadYaw");
			forcedBodyYaw = tag.getFloat("ForcedBodyYaw");
			forcedPitch = tag.getFloat("ForcedPitch");
			forcedLimbDistance = tag.getFloat("ForceLimbDistance");
			forcedLimbAngle = tag.getFloat("ForcedLimbAngle");
		}
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if (frozen) {
			tag.putBoolean("Frozen", frozen);
			tag.putInt("TicksFrozen", ticksFrozen);
			tag.putString("ForcedPose", forcedPose.toString());
			tag.putFloat("ForcedHeadYaw", forcedHeadYaw);
			tag.putFloat("ForcedBodyYaw", forcedBodyYaw);
			tag.putFloat("ForcedPitch", forcedPitch);
			tag.putFloat("ForceLimbDistance", forcedLimbDistance);
			tag.putFloat("ForcedLimbAngle", forcedLimbAngle);
		}
	}

	@Override
	public void serverTick() {
		if (frozen && obj instanceof MobEntity mob) {
			if (!mob.isAiDisabled()) {
				mob.setAiDisabled(true);
			}
			ticksFrozen++;
			if (ticksFrozen > 200 && obj.getRandom().nextFloat() < 1 / 64F && !mob.isPersistent()) {
				obj.damage(DamageSource.GENERIC, 2);
			}
			if (obj.horizontalCollision && obj.getVelocity().length() >= 0.05) {
				obj.damage(DamageSource.FLY_INTO_WALL, 2);
			}
			if (ticksFrozen <= 10) {
				obj.setVelocity(obj.getVelocity().multiply(0.25));
			}
			if (!obj.hasNoGravity()) {
				obj.setVelocity(obj.getVelocity().add(0, -0.02, 0));
			}
			obj.move(MovementType.SELF, obj.getVelocity());
		}
	}

	public void sync() {
		ModEntityComponents.FROZEN.sync(obj);
	}

	public Entity getLastFreezingAttacker() {
		return lastFreezingAttacker;
	}

	public void setLastFreezingAttacker(Entity lastFreezingAttacker) {
		this.lastFreezingAttacker = lastFreezingAttacker;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public EntityPose getForcedPose() {
		return forcedPose;
	}

	public float getForcedHeadYaw() {
		return forcedHeadYaw;
	}

	public float getForcedBodyYaw() {
		return forcedBodyYaw;
	}

	public float getForcedPitch() {
		return forcedPitch;
	}

	public float getForcedLimbDistance() {
		return forcedLimbDistance;
	}

	public float getForcedLimbAngle() {
		return forcedLimbAngle;
	}

	public boolean shouldFreezeOnDeath(DamageSource source) {
		if (!obj.world.isClient && !obj.getType().isIn(ModTags.EntityTypes.CANNOT_FREEZE) && lastFreezingAttacker != null) {
			return FrozenComponent.isSourceFreezingEntity(source) || source == DamageSource.FREEZE;
		}
		return false;
	}

	public void freeze() {
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_FREEZE, 1, 1);
		obj.setSilent(true);
		obj.setHealth(1);
		forcedPose = obj.getPose();
		forcedHeadYaw = obj.headYaw;
		forcedBodyYaw = obj.bodyYaw;
		forcedPitch = obj.getPitch();
		forcedLimbDistance = obj.limbDistance;
		forcedLimbAngle = obj.limbAngle;
		setFrozen(true);
		sync();
	}

	public static boolean shouldHurt(Entity attacker, Entity hitEntity) {
		if (attacker == null) {
			return true;
		}
		if (attacker == hitEntity) {
			return false;
		}
		if (attacker instanceof PlayerEntity attackingPlayer && hitEntity instanceof PlayerEntity hitPlayer && !attackingPlayer.shouldDamagePlayer(hitPlayer)) {
			return false;
		}
		NbtCompound tag = hitEntity.writeNbt(new NbtCompound());
		return !tag.contains("Owner") || !tag.getUuid("Owner").equals(attacker.getUuid());
	}

	public static boolean isSourceFreezingEntity(DamageSource source) {
		return (source instanceof EntityDamageSource && source.name.equals("freeze")) || (source.getSource() instanceof LivingEntity living && EnchantmentHelper.getEquipmentLevel(ModEnchantments.FROSTBITE, living) > 0);
	}
}
