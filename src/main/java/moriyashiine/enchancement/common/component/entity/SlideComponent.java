package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.slide.EntityAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class SlideComponent implements CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldSlide = false;
	private int ticksSliding = 0;

	private boolean hasSlide = false;

	public SlideComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(@NotNull NbtCompound tag) {
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
	}

	@Override
	public void tick() {
		hasSlide = EnchancementUtil.hasEnchantment(ModEnchantments.SLIDE, obj);
		if (hasSlide) {
			if (obj.isSneaking()) {
				if (obj.isSprinting()) {
					shouldSlide = true;
					obj.setSprinting(false);
				}
				if (shouldSlide) {
					boolean onGround = obj.isOnGround();
					if (ticksSliding <= 40) {
						if (onGround) {
							((EntityAccessor) obj).enchancement$spawnSprintingParticles();
						} else {
							obj.airStrafingSpeed *= 10;
						}
					}
					ticksSliding = MathHelper.clamp(ticksSliding + (onGround ? 1 : -4), 0, 60);
				}
			} else {
				shouldSlide = false;
				if (ticksSliding > 0) {
					ticksSliding = Math.max(0, ticksSliding - 2);
				}
			}
		} else {
			shouldSlide = false;
			ticksSliding = 0;
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (shouldSlide && ticksSliding <= 40) {
			EnchancementUtil.PACKET_IMMUNITIES.put(obj, 20);
		}
	}

	public boolean shouldSlide() {
		return shouldSlide;
	}

	public void setShouldSlide(boolean shouldSlide) {
		this.shouldSlide = shouldSlide;
	}

	public int getTicksSliding() {
		return ticksSliding;
	}

	public void setTicksSliding(int ticksSliding) {
		this.ticksSliding = ticksSliding;
	}

	public boolean hasSlide() {
		return hasSlide;
	}
}
