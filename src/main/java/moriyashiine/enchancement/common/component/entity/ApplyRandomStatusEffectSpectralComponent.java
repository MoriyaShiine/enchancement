/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class ApplyRandomStatusEffectSpectralComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final SpectralArrowEntity obj;
	private List<StatusEffectInstance> effects = new ArrayList<>();
	private int color = -1;

	public ApplyRandomStatusEffectSpectralComponent(SpectralArrowEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		if (tag.contains("CustomPotionEffects")) {
			NbtList nbtList = tag.getList("CustomPotionEffects", NbtElement.COMPOUND_TYPE);
			for (int i = 0; i < nbtList.size(); i++) {
				effects.add(StatusEffectInstance.fromNbt(nbtList.getCompound(i)));
			}
			color = tag.getInt("Color");
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		if (!effects.isEmpty()) {
			NbtList nbtList = new NbtList();
			for (StatusEffectInstance statusEffectInstance : effects) {
				nbtList.add(statusEffectInstance.writeNbt());
			}
			tag.put("CustomPotionEffects", nbtList);
			tag.putInt("Color", color);
		}
	}

	@Override
	public void tick() {
		if (obj.isInGround() && !effects.isEmpty() && obj.inGroundTime >= 600) {
			effects.clear();
			color = -1;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (obj.isInGround()) {
			if (obj.inGroundTime % 5 == 0) {
				spawnParticles(1);
			}
		} else {
			spawnParticles(2);
		}
	}

	public void sync() {
		ModEntityComponents.APPLY_RANDOM_STATUS_EFFECT_SPECTRAL.sync(obj);
	}

	public List<StatusEffectInstance> getEffects() {
		return effects;
	}

	public void setEffects(List<StatusEffectInstance> effects) {
		this.effects = effects;
		color = PotionContentsComponent.getColor(effects);
	}

	public int getColor() {
		return color;
	}

	private void spawnParticles(int amount) {
		int color = getColor();
		if (color == -1 || amount <= 0) {
			return;
		}
		for (int i = 0; i < amount; i++) {
			obj.getWorld().addParticle(EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, color), obj.getParticleX(0.5), obj.getRandomBodyY(), obj.getParticleZ(0.5), 0, 0, 0);
		}
	}
}
