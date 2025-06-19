/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.entity.mob;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
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
			setSlim(SLIM_STATUSES.removeBoolean(getUuid()));
		}
	}

	@Override
	public boolean shouldRenderName() {
		return true;
	}

	@Override
	protected void readCustomData(ReadView view) {
		super.readCustomData(view);
		setSlim(view.getBoolean("Slim", false));
	}

	@Override
	protected void writeCustomData(WriteView view) {
		super.writeCustomData(view);
		view.putBoolean("Slim", isSlim());
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(SLIM, false);
	}

	public boolean isSlim() {
		return dataTracker.get(SLIM);
	}

	public void setSlim(boolean slim) {
		dataTracker.set(SLIM, slim);
	}
}
