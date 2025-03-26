/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
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
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		effects.clear();
		effects.addAll(tag.get("CustomPotionEffects", StatusEffectInstance.CODEC.listOf(), registryLookup.getOps(NbtOps.INSTANCE)).orElse(List.of()));
		color = tag.getInt("Color", -1);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.put("CustomPotionEffects", StatusEffectInstance.CODEC.listOf(), registryLookup.getOps(NbtOps.INSTANCE), List.copyOf(effects));
		tag.putInt("Color", color);
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
			obj.getWorld().addParticleClient(EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, color), obj.getParticleX(0.5), obj.getRandomBodyY(), obj.getParticleZ(0.5), 0, 0, 0);
		}
	}
}
