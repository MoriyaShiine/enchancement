/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.resources.sound;

import moriyashiine.enchancement.common.component.entity.LightningDashComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.world.entity.Entity;

public class SparkSoundInstance extends AbstractTickableSoundInstance {
	private final Entity entity;

	public SparkSoundInstance(Entity entity) {
		super(ModSoundEvents.ENTITY_GENERIC_SPARK, entity.getSoundSource(), entity.getRandom());
		this.entity = entity;
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		looping = true;
		delay = 0;
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
				stop();
			}
			return;
		}
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
	}
}
