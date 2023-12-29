/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;

public class ExtendedWaterComponent implements CommonTickingComponent {
	private int ticksLeft = 0;

	private final LivingEntity obj;

	public ExtendedWaterComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		ticksLeft = tag.getInt("TicksLeft");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("TicksLeft", ticksLeft);
	}

	@Override
	public void tick() {
		if (ticksLeft > 0) {
			ticksLeft--;
			if (obj.isOnFire()) {
				obj.extinguishWithSound();
			}
		}
		if (shouldCount()) {
			if (obj.isWet()) {
				ticksLeft = 100;
			}
		} else {
			ticksLeft = 0;
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (ticksLeft > 0 && obj.age % 10 == 0 && !obj.isWet()) {
			obj.getWorld().playSound(null, obj.getBlockPos(), SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_WATER, obj.getSoundCategory(), 1, 1);
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (ticksLeft > 0 && !obj.isInvisible() && !obj.isWet()) {
			if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || MinecraftClient.getInstance().player != obj) {
				obj.getWorld().addParticle(ParticleTypes.FALLING_WATER, obj.getParticleX(1), obj.getRandomBodyY(), obj.getParticleZ(1), 0, 0, 0);
			}
		}
	}

	public int getTicksLeft() {
		return ticksLeft;
	}

	private boolean shouldCount() {
		return EnchancementUtil.hasEnchantment(ModEnchantments.AMPHIBIOUS, obj) || EnchancementUtil.hasEnchantment(ModEnchantments.BUOY, obj);
	}
}
