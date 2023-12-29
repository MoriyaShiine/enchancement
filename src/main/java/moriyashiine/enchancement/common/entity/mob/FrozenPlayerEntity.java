/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.entity.mob;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.UUID;

public class FrozenPlayerEntity extends MobEntity {
	public static final Object2BooleanMap<UUID> SLIM_STATUSES = new Object2BooleanOpenHashMap<>();

	public static final TrackedData<Boolean> SLIM = DataTracker.registerData(FrozenPlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public FrozenPlayerEntity(EntityType<? extends MobEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void tick() {
		super.tick();
		if (!getWorld().isClient && SLIM_STATUSES.containsKey(getUuid())) {
			dataTracker.set(SLIM, SLIM_STATUSES.removeBoolean(getUuid()));
		}
	}

	@Override
	protected Identifier getLootTableId() {
		return LootTables.EMPTY;
	}

	@Override
	public boolean shouldRenderName() {
		return true;
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		dataTracker.set(SLIM, nbt.getBoolean("Slim"));
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("Slim", dataTracker.get(SLIM));
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(SLIM, false);
	}
}
