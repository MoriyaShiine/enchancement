/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.resources.sound;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.EMeterComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public class EMeterSoundInstance extends AbstractTickableSoundInstance {
	private final Entity entity;
	private final UUID floatingUuid;

	public EMeterSoundInstance(Entity entity, UUID floatingUuid) {
		super(ModSoundEvents.GENERIC_E_METER_FLOAT, entity.getSoundSource(), entity.getRandom());
		this.entity = entity;
		this.floatingUuid = floatingUuid;
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		volume = 0.001F;
		pitch = Mth.nextFloat(entity.getRandom(), 0.85F, 1.15F);
		looping = true;
	}

	@Override
	public void tick() {
		boolean done = entity == null || !entity.isAlive();
		if (!done) {
			EMeterComponent eMeterComponent = ModEntityComponents.E_METER.getNullable(entity);
			if (eMeterComponent == null || !eMeterComponent.isFloatingUuid(floatingUuid)) {
				done = true;
			}
		}
		if (done) {
			volume -= 1 / 6F;
			if (volume < 0) {
				stop();
			}
			return;
		} else if (volume < 1) {
			volume = Math.min(1, volume + 1 / 6F);
		}
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
	}
}
