/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class ExtendedWaterComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private int ticksWet = 0;

	private boolean hasAmphibious = false, hasBuoy = false;

	public ExtendedWaterComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		ticksWet = tag.getInt("TicksWet");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("TicksWet", ticksWet);
	}

	@Override
	public void tick() {
		hasAmphibious = EnchancementUtil.hasEnchantment(ModEnchantments.AMPHIBIOUS, obj);
		hasBuoy = EnchancementUtil.hasEnchantment(ModEnchantments.BUOY, obj);
		if (shouldCount()) {
			if (obj.isWet()) {
				markWet();
			}
		} else {
			ticksWet = 0;
		}
		if (ticksWet > 0) {
			ticksWet--;
			if (hasAmphibious && obj.isOnFire()) {
				obj.extinguishWithSound();
			}
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (ticksWet > 0 && obj.age % 10 == 0 && !obj.isWet()) {
			obj.getWorld().playSound(null, obj.getBlockPos(), SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_WATER, obj.getSoundCategory(), 1, 1);
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (ticksWet > 0 && !obj.isInvisible() && !obj.isWet()) {
			if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || MinecraftClient.getInstance().player != obj) {
				if (hasAmphibious) {
					obj.getWorld().addParticle(ParticleTypes.FALLING_WATER, obj.getParticleX(1), obj.getY() + obj.getHeight() * MathHelper.nextDouble(obj.getRandom(), 0.4, 0.8), obj.getParticleZ(1), 0, 0, 0);
				}
				if (hasBuoy) {
					obj.getWorld().addParticle(ParticleTypes.FALLING_WATER, obj.getParticleX(1), obj.getY() + obj.getHeight() * 0.15, obj.getParticleZ(1), 0, 0, 0);
				}
			}
		}
	}

	public void sync() {
		ModEntityComponents.EXTENDED_WATER.sync(obj);
	}

	public int getTicksWet() {
		return ticksWet;
	}

	public void markWet() {
		ticksWet = 160;
	}

	public boolean hasAmphibious() {
		return hasAmphibious;
	}

	public boolean shouldCount() {
		return hasAmphibious || hasBuoy;
	}
}
