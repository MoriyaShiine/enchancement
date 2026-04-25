/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.resources.sound;

import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

public class BrimstoneFireSoundInstance extends AbstractTickableSoundInstance {
	private final Entity entity;
	private final UUID uuid;
	private int age = 0;

	public BrimstoneFireSoundInstance(Entity entity, UUID uuid) {
		super(ModSoundEvents.ITEM_CROSSBOW_LOADING_BRIMSTONE, entity.getSoundSource(), entity.getRandom());
		this.entity = entity;
		this.uuid = uuid;
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		looping = true;
		delay = 0;
	}

	@Override
	public void tick() {
		if (age > 1 && !isEntityUsing(entity)) {
			stop();
			return;
		}
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		age++;
		pitch = Mth.lerp(age / 60F, 1, 2);
	}

	private boolean isEntityUsing(Entity entity) {
		if (entity == null || entity.isRemoved()) {
			return false;
		}
		if (entity instanceof LivingEntity living) {
			if (living.isDeadOrDying()) {
				return false;
			}
			return uuid.equals(living.getUseItem().get(ModComponentTypes.BRIMSTONE_UUID));
		}
		return false;
	}
}
