/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.sound;

import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class BrimstoneTravelSoundInstance extends MovingSoundInstance {
	private final BrimstoneEntity entity;
	private Vec3d previousPos;
	private float pitchModifier = 0;

	public BrimstoneTravelSoundInstance(BrimstoneEntity entity, SoundCategory soundCategory) {
		super(ModSoundEvents.ENTITY_BRIMSTONE_TRAVEL, soundCategory, Random.create());
		this.entity = entity;
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		repeat = true;
		repeatDelay = 0;
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
				setDone();
			}
			return;
		}
		Vec3d newPos = entity.getPos().add(entity.getRotationVector().multiply(entity.distanceTraveled));
		x = newPos.getX();
		y = newPos.getY();
		z = newPos.getZ();
		if (previousPos != null) {
			Vec3d cameraPos = MinecraftClient.getInstance().getCameraEntity().getPos();
			double prevDistance = previousPos.distanceTo(cameraPos);
			if (prevDistance < volume * 16) {
				if (prevDistance > newPos.distanceTo(cameraPos)) {
					pitchModifier += 1 / 40F;
				} else {
					pitchModifier -= 1 / 40F;
				}
			}
		}
		pitchModifier = MathHelper.clamp(pitchModifier, 0, 0.5F);
		pitch = MathHelper.lerp((float) entity.getDamage() / 12F, 1.5F, 1) + pitchModifier;
		previousPos = newPos;
	}
}
