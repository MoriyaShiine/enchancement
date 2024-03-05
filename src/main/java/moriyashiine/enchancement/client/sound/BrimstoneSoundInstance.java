/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.sound;

import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

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
		if (age > 1 && !isEntityUsing(entity)) {
			setDone();
			return;
		}
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		age++;
		pitch = MathHelper.lerp(age / 60F, 1F, 2F);
	}

	private boolean isEntityUsing(Entity entity) {
		if (entity == null || entity.isRemoved()) {
			return false;
		}
		if (entity instanceof LivingEntity living) {
			if (living.isDead()) {
				return false;
			}
			return uuid.equals(EnchancementUtil.getBrimstoneUUID(living.getActiveItem()));
		}
		return false;
	}
}
