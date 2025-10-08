/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.enchantment.effect.LeechingTridentEffect;
import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.Collections;

public class LeechingTridentComponent implements AutoSyncedComponent, CommonTickingComponent {
	private static final int NO_ENTITY = -1;

	private final TridentEntity obj;
	private LeechData leechData = null;
	private LivingEntity stuckEntity = null;
	private int stuckEntityId = NO_ENTITY;
	private int leechingTicks = 0, stabTicks = 0;

	public LeechingTridentComponent(TridentEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		leechData = readView.read("LeechData", LeechData.CODEC).orElse(null);
		stuckEntityId = readView.getInt("StuckEntityId", NO_ENTITY);
		leechingTicks = readView.getInt("LeechingTicks", 0);
		stabTicks = readView.getInt("StabTicks", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		if (leechData != null) {
			writeView.put("LeechData", LeechData.CODEC, leechData);
		}
		writeView.putInt("StuckEntityId", stuckEntityId);
		writeView.putInt("LeechingTicks", leechingTicks);
		writeView.putInt("StabTicks", stabTicks);
	}

	@Override
	public void tick() {
		if (stuckEntityId == NO_ENTITY) {
			if (stuckEntity != null) {
				setStuckEntity(null);
			}
		} else if (stuckEntity == null && obj.getEntityWorld().getEntityById(stuckEntityId) instanceof LivingEntity living) {
			setStuckEntity(living);
		}
		if (stuckEntity != null && stuckEntity.isPartOfGame()) {
			obj.refreshPositionAfterTeleport(stuckEntity.getX(), stuckEntity.getEyeY(), stuckEntity.getZ());
			obj.setVelocity(Vec3d.ZERO);
			leechingTicks++;
			if (stabTicks > 0) {
				stabTicks--;
			}
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (stuckEntity != null && stuckEntity.isPartOfGame()) {
			if (leechingTicks % 20 == 0) {
				int timeUntilRegen = stuckEntity.timeUntilRegen;
				stuckEntity.timeUntilRegen = 0;
				if (stuckEntity.damage((ServerWorld) obj.getEntityWorld(), obj.getEntityWorld().getDamageSources().create(ModDamageTypes.LIFE_DRAIN, obj, obj.getOwner()), leechData.damage()) && obj.getOwner() instanceof LivingEntity living && living.isPartOfGame()) {
					living.heal(leechData.healAmount());
				}
				stuckEntity.timeUntilRegen = timeUntilRegen;
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
		if (stuckEntity != null && stuckEntity.isPartOfGame() && stabTicks == 19) {
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
		if (entity instanceof TridentEntity) {
			MutableFloat damage = new MutableFloat(), healAmount = new MutableFloat(), duration = new MutableFloat();
			if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.LEECHING_TRIDENT)) {
				LeechingTridentEffect.setValues(user.getRandom(), damage, healAmount, duration, Collections.singleton(stack));
			} else if (!(user instanceof PlayerEntity) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.LEECHING_TRIDENT)) {
				LeechingTridentEffect.setValues(user.getRandom(), damage, healAmount, duration, EnchancementUtil.getHeldItems(user));
			}
			if (damage.floatValue() != 0) {
				LeechingTridentComponent leechingTridentComponent = ModEntityComponents.LEECHING_TRIDENT.get(entity);
				leechingTridentComponent.leechData = new LeechData(damage.floatValue(), healAmount.floatValue(), MathHelper.floor(duration.floatValue() * 20));
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
