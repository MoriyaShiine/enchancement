/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.packet.BuoyPacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.LivingEntityAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;

public class BuoyComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shoudBoost = false;
	private float boost = 0;

	private boolean hasBuoy = false;

	public BuoyComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		shoudBoost = tag.getBoolean("ShouldBoost");
		boost = tag.getFloat("Boost");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("ShouldBoost", shoudBoost);
		tag.putFloat("Boost", boost);
	}

	@Override
	public void tick() {
		hasBuoy = EnchancementUtil.hasEnchantment(ModEnchantments.BUOY, obj);
		if (hasBuoy) {
			if (shoudBoost) {
				if (EnchancementUtil.isSubmerged(obj, true, true) && EnchancementUtil.isGroundedOrAirborne(obj, true)) {
					boost = (float) MathHelper.clamp(boost + 0.0025, 0.05, 2);
					obj.addVelocity(0, boost, 0);
				} else {
					shoudBoost = false;
					boost = 0;
				}
			} else {
				boost = 0;
			}
		} else {
			shoudBoost = false;
			boost = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasBuoy) {
			if (shoudBoost) {
				double x = obj.getX();
				double y = obj.getY();
				double z = obj.getZ();
				ParticleEffect bubbleColumn = ParticleTypes.BUBBLE_COLUMN_UP, splash = ParticleTypes.SPLASH, bubble = ParticleTypes.BUBBLE;
				if (EnchancementUtil.isSubmerged(obj, false, true)) {
					bubbleColumn = ParticleTypes.LAVA;
					splash = ParticleTypes.LAVA;
					bubble = ParticleTypes.LAVA;
				}
				obj.getWorld().addParticle(bubbleColumn, x, y, z, 0, 0.04, 0);
				obj.getWorld().addParticle(bubbleColumn, obj.getParticleX(0.5), y + obj.getHeight() / 8, obj.getParticleZ(0.5), 0, 0.04, 0);
				if (obj.getWorld().getBlockState(obj.getBlockPos().up()).isAir()) {
					for (int i = 0; i < 2; i++) {
						obj.getWorld().addParticle(splash, obj.getParticleX(0.5), obj.getBlockY() + 1, obj.getParticleZ(0.5), 0, 1, 0);
						obj.getWorld().addParticle(bubble, obj.getParticleX(0.5), obj.getBlockY() + 1, obj.getParticleZ(0.5), 0, 0.2, 0);
					}
				}
			}
			if (((LivingEntityAccessor) obj).enchancement$jumping()) {
				if (!shoudBoost && EnchancementUtil.isSubmerged(obj, true, true)) {
					shoudBoost = true;
					BuoyPacket.send(true);
				}
			} else if (shoudBoost) {
				shoudBoost = false;
				BuoyPacket.send(false);
			}
		}
	}

	public void setShoudBoost(boolean shoudBoost) {
		this.shoudBoost = shoudBoost;
	}

	public boolean hasBuoy() {
		return hasBuoy;
	}
}
