package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.mixin.slide.EntityAccessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class SlideComponent implements CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldSlide = false;
	private int ticksSliding = 0;

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
		if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.SLIDE, obj) > 0) {
			if (obj.isSneaking()) {
				if (obj.isSprinting()) {
					shouldSlide = true;
					obj.setSprinting(false);
				}
				if (shouldSlide) {
					if (ticksSliding < 60) {
						if (ticksSliding <= 40) {
							((EntityAccessor) obj).enchancement$spawnSprintingParticles();
						}
						ticksSliding++;
					}
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

	public boolean shouldSlide() {
		return shouldSlide;
	}

	public int getTicksSliding() {
		return ticksSliding;
	}
}
