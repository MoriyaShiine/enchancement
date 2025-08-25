/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.TintedParticleEffect;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class ApplyRandomStatusEffectGenericComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PersistentProjectileEntity obj;
	private List<StatusEffectInstance> effects = new ArrayList<>();
	private int color = -1;

	public ApplyRandomStatusEffectGenericComponent(PersistentProjectileEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		effects.clear();
		effects.addAll(readView.read("CustomPotionEffects", StatusEffectInstance.CODEC.listOf()).orElse(List.of()));
		color = readView.getInt("Color", -1);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.put("CustomPotionEffects", StatusEffectInstance.CODEC.listOf(), effects);
		writeView.putInt("Color", color);
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
		ModEntityComponents.APPLY_RANDOM_STATUS_EFFECT_GENERIC.sync(obj);
	}

	public List<StatusEffectInstance> getEffects() {
		return effects;
	}

	public void setEffects(List<StatusEffectInstance> effects) {
		this.effects = effects;
		color = PotionContentsComponent.mixColors(effects).orElse(-1);
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
			obj.getWorld().addParticleClient(TintedParticleEffect.create(ParticleTypes.ENTITY_EFFECT, color), obj.getParticleX(0.5), obj.getRandomBodyY(), obj.getParticleZ(0.5), 0, 0, 0);
		}
	}
}
