/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.sound;

import moriyashiine.enchancement.common.registry.ModSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class BrimstoneSoundInstance extends MovingSoundInstance {
	private final Entity entity;
	private final UUID uuid;
	private int age = 0;

	public BrimstoneSoundInstance(Entity entity, UUID uuid, SoundCategory soundCategory) {
		super(ModSoundEvents.ITEM_CROSSBOW_LOADING_BRIMSTONE, soundCategory, entity.getWorld().random);
		this.entity = entity;
		this.uuid = uuid;
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		repeat = true;
		repeatDelay = 0;
	}

	@Override
	public void tick() {
		if (entity == null || entity.isRemoved() || (entity instanceof LivingEntity living && living.isDead())) {
			setDone();
			return;
		}
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		age++;
		pitch = MathHelper.lerp(age / 60F, 1, 2);
	}

	public UUID getUuid() {
		return uuid;
	}
}
