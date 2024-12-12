/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.sound;

import moriyashiine.enchancement.common.component.entity.LightningDashComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.Entity;

public class SparkSoundInstance extends MovingSoundInstance {
	private final Entity entity;

	public SparkSoundInstance(Entity entity) {
		super(ModSoundEvents.ENTITY_GENERIC_SPARK, entity.getSoundCategory(), entity.getRandom());
		this.entity = entity;
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		repeat = true;
		repeatDelay = 0;
	}

	@Override
	public void tick() {
		boolean done = entity == null || !entity.isAlive();
		if (!done) {
			LightningDashComponent lightningDashComponent = ModEntityComponents.LIGHTNING_DASH.getNullable(entity);
			if (lightningDashComponent == null || !lightningDashComponent.isFloating()) {
				done = true;
			}
		}
		if (done) {
			volume -= 0.1F;
			if (volume < 0) {
				setDone();
			}
			return;
		}
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
	}
}
