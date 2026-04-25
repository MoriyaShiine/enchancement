/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class ApplyRandomMobEffectGenericComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final AbstractArrow obj;
	private List<MobEffectInstance> effects = new ArrayList<>();
	private int color = -1;

	public ApplyRandomMobEffectGenericComponent(AbstractArrow obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		effects.clear();
		effects.addAll(input.read("CustomPotionEffects", MobEffectInstance.CODEC.listOf()).orElse(List.of()));
		color = input.getIntOr("Color", -1);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.store("CustomPotionEffects", MobEffectInstance.CODEC.listOf(), effects);
		output.putInt("Color", color);
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
		ModEntityComponents.APPLY_RANDOM_MOB_EFFECT_GENERIC.sync(obj);
	}

	public List<MobEffectInstance> getEffects() {
		return effects;
	}

	public void setEffects(List<MobEffectInstance> effects) {
		this.effects = effects;
		color = PotionContents.getColorOptional(effects).orElse(-1);
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
			obj.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, color), obj.getRandomX(0.5), obj.getRandomY(), obj.getRandomZ(0.5), 0, 0, 0);
		}
	}
}
