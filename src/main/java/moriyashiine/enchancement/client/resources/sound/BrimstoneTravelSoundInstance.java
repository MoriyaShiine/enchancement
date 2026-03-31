/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.resources.sound;

import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.Brimstone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class BrimstoneTravelSoundInstance extends AbstractTickableSoundInstance {
	private final Brimstone entity;
	private Vec3 previousPos;
	private float pitchModifier = 0;

	public BrimstoneTravelSoundInstance(Brimstone entity) {
		super(ModSoundEvents.ENTITY_BRIMSTONE_TRAVEL, entity.getSoundSource(), entity.getRandom());
		this.entity = entity;
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		looping = true;
		delay = 0;
		volume = 8;
	}

	@Override
	public void tick() {
		if (entity == null || entity.isRemoved()) {
			if (volume > 1) {
				volume = Math.max(1, volume - 4);
			} else {
				volume -= 0.1F;
			}
			if (volume < 0) {
				stop();
			}
			return;
		}
		Vec3 pos = entity.position().add(entity.getLookAngle().scale(entity.distanceTraveled));
		x = pos.x();
		y = pos.y();
		z = pos.z();
		if (previousPos != null) {
			Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
			if (cameraEntity != null) {
				Vec3 cameraPos = cameraEntity.position();
				double prevDistance = previousPos.distanceTo(cameraPos);
				if (prevDistance < volume * 16) {
					if (prevDistance > pos.distanceTo(cameraPos)) {
						pitchModifier += 1 / 40F;
					} else {
						pitchModifier -= 1 / 40F;
					}
				}
			}
		}
		pitchModifier = Mth.clamp(pitchModifier, 0, 0.5F);
		pitch = Mth.lerp(entity.getDamage() / 12F, 1.5F, 1) + pitchModifier;
		previousPos = pos;
	}
}
