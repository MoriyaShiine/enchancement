/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.LeechingTridentEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jspecify.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.Collections;

public class LeechingTridentComponent implements AutoSyncedComponent, CommonTickingComponent {
	private static final int NO_ENTITY = -1;

	private final ThrownTrident obj;
	private LeechData leechData = null;
	private LivingEntity stuckEntity = null;
	private int stuckEntityId = NO_ENTITY;
	private int leechingTicks = 0, stabTicks = 0;

	public LeechingTridentComponent(ThrownTrident obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		leechData = input.read("LeechData", LeechData.CODEC).orElse(null);
		stuckEntityId = input.getIntOr("StuckEntityId", NO_ENTITY);
		leechingTicks = input.getIntOr("LeechingTicks", 0);
		stabTicks = input.getIntOr("StabTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.storeNullable("LeechData", LeechData.CODEC, leechData);
		output.putInt("StuckEntityId", stuckEntityId);
		output.putInt("LeechingTicks", leechingTicks);
		output.putInt("StabTicks", stabTicks);
	}

	@Override
	public void tick() {
		if (stuckEntityId == NO_ENTITY) {
			if (stuckEntity != null) {
				setStuckEntity(null);
			}
		} else if (stuckEntity == null && obj.level().getEntity(stuckEntityId) instanceof LivingEntity living) {
			setStuckEntity(living);
		}
		if (stuckEntity != null && stuckEntity.slib$exists()) {
			obj.snapTo(stuckEntity.getX(), stuckEntity.getEyeY(), stuckEntity.getZ());
			obj.setDeltaMovement(Vec3.ZERO);
			leechingTicks++;
			if (stabTicks > 0) {
				stabTicks--;
			}
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (stuckEntity != null && stuckEntity.slib$exists()) {
			if (leechingTicks % 20 == 0) {
				int timeUntilRegen = stuckEntity.invulnerableTime;
				stuckEntity.invulnerableTime = 0;
				if (stuckEntity.hurtServer((ServerLevel) obj.level(), obj.level().damageSources().source(ModDamageTypes.LIFE_DRAIN, obj, obj.getOwner()), leechData.damage()) && obj.getOwner() instanceof LivingEntity living && living.slib$exists()) {
					living.heal(leechData.healAmount());
				}
				stuckEntity.invulnerableTime = timeUntilRegen;
				stabTicks = 20;
				sync();
			}
			if (leechingTicks >= leechData.maxTicks()) {
				unleech();
			}
		} else if (stuckEntityId != NO_ENTITY) {
			unleech();
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (stuckEntity != null && stuckEntity.slib$exists() && stabTicks == 19) {
			SLibClientUtils.addParticles(stuckEntity, ParticleTypes.DAMAGE_INDICATOR, 5, ParticleAnchor.BODY);
		}
	}

	public void sync() {
		ModEntityComponents.LEECHING_TRIDENT.sync(obj);
	}

	public boolean hasLeech() {
		return leechData != null;
	}

	public LivingEntity getStuckEntity() {
		return stuckEntity;
	}

	public void setStuckEntity(@Nullable LivingEntity stuckEntity) {
		this.stuckEntity = stuckEntity;
		stuckEntityId = stuckEntity == null ? NO_ENTITY : stuckEntity.getId();
	}

	public int getLeechingTicks() {
		return leechingTicks;
	}

	public int getStabTicks() {
		return stabTicks;
	}

	public void unleech() {
		stuckEntityId = NO_ENTITY;
		leechingTicks = 0;
		stabTicks = 0;
		sync();
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, Entity entity) {
		if (entity instanceof ThrownTrident) {
			MutableFloat damage = new MutableFloat(), healAmount = new MutableFloat(), duration = new MutableFloat();
			if (EnchantmentHelper.has(stack, ModEnchantmentEffectComponentTypes.LEECHING_TRIDENT)) {
				LeechingTridentEffect.setValues(user.getRandom(), damage, healAmount, duration, Collections.singleton(stack));
			} else if (!(user instanceof Player) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.LEECHING_TRIDENT)) {
				LeechingTridentEffect.setValues(user.getRandom(), damage, healAmount, duration, EnchancementUtil.getHeldItems(user));
			}
			if (damage.floatValue() != 0) {
				LeechingTridentComponent leechingTridentComponent = ModEntityComponents.LEECHING_TRIDENT.get(entity);
				leechingTridentComponent.leechData = new LeechData(damage.floatValue(), healAmount.floatValue(), Mth.floor(duration.floatValue() * 20));
				leechingTridentComponent.sync();
			}
		}
	}

	public record LeechData(float damage, float healAmount, int maxTicks) {
		public static final Codec<LeechData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Codec.FLOAT.fieldOf("damage").forGetter(LeechData::damage),
						Codec.FLOAT.fieldOf("heal_amount").forGetter(LeechData::healAmount),
						Codec.INT.fieldOf("max_ticks").forGetter(LeechData::maxTicks))
				.apply(instance, LeechData::new));
	}
}
