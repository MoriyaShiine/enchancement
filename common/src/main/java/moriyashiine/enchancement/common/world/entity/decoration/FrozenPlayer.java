package moriyashiine.enchancement.common.world.entity.decoration;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.UUID;

public class FrozenPlayer extends Mob {
	public static final Object2BooleanMap<UUID> SLIM_STATUSES = new Object2BooleanOpenHashMap<>();

	public static final EntityDataAccessor<Boolean> SLIM = SynchedEntityData.defineId(FrozenPlayer.class, EntityDataSerializers.BOOLEAN);

	public FrozenPlayer(EntityType<FrozenPlayer> type, Level level) {
		super(type, level);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide() && SLIM_STATUSES.containsKey(getUUID())) {
			setSlim(SLIM_STATUSES.removeBoolean(getUUID()));
		}
	}

	@Override
	public boolean shouldShowName() {
		return true;
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		setSlim(input.getBooleanOr("Slim", false));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("Slim", isSlim());
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder entityData) {
		super.defineSynchedData(entityData);
		entityData.define(SLIM, false);
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
		if (firstTick) {
			return false;
		}
		return super.hurtServer(level, source, damage);
	}

	public boolean isSlim() {
		return entityData.get(SLIM);
	}

	public void setSlim(boolean slim) {
		entityData.set(SLIM, slim);
	}
}
