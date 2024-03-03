/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.mixin.chaos.PersistentProjectileEntityAccessor;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChaosSpectralArrowComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final SpectralArrowEntity obj;
	private List<StatusEffectInstance> effects = new ArrayList<>();
	private int color = -1;

	public ChaosSpectralArrowComponent(SpectralArrowEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		if (tag.contains("CustomPotionEffects")) {
			effects.addAll(PotionUtil.getCustomPotionEffects(tag));
			color = tag.getInt("Color");
		}
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if (!effects.isEmpty()) {
			NbtList nbtList = new NbtList();
			for (StatusEffectInstance statusEffectInstance : effects) {
				nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
			}
			tag.put("CustomPotionEffects", nbtList);
			tag.putInt("Color", color);
		}
	}

	@Override
	public void tick() {
		if (((PersistentProjectileEntityAccessor) obj).enchancement$inGround() && !effects.isEmpty() && ((PersistentProjectileEntityAccessor) obj).enchancement$inGroundTime() >= 600) {
			effects.clear();
			color = -1;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (((PersistentProjectileEntityAccessor) obj).enchancement$inGround()) {
			if (((PersistentProjectileEntityAccessor) obj).enchancement$inGroundTime() % 5 == 0) {
				spawnParticles(1);
			}
		} else {
			spawnParticles(2);
		}
	}

	public void sync() {
		ModEntityComponents.CHAOS_SPECTRAL_ARROW.sync(obj);
	}

	public List<StatusEffectInstance> getEffects() {
		return effects;
	}

	public void setEffects(List<StatusEffectInstance> effects) {
		this.effects = effects;
		color = PotionUtil.getColor(effects);
	}

	public int getColor() {
		return color;
	}

	private void spawnParticles(int amount) {
		int color = getColor();
		if (color == -1 || amount <= 0) {
			return;
		}
		double r = (color >> 16 & 0xFF) / 255D;
		double g = (color >> 8 & 0xFF) / 255D;
		double b = (color & 0xFF) / 255D;
		for (int i = 0; i < amount; i++) {
			obj.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, obj.getParticleX(0.5), obj.getRandomBodyY(), obj.getParticleZ(0.5), r, g, b);
		}
	}
}
