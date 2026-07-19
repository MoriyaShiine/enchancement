package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.EnchancementDamageTypes;
import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.LeechingTridentEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
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
	private final ThrownTrident obj;
	private LeechData leechData = null;
	private EntityReference<LivingEntity> stuckEntity = null;
	private boolean hasStuck = false;
	private int leechingTicks = 0, stabTicks = 0;

	public LeechingTridentComponent(ThrownTrident obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		leechData = input.read("LeechData", LeechData.CODEC).orElse(null);
		stuckEntity = EntityReference.read(input, "StuckEntity");
		hasStuck = input.getBooleanOr("HasStuck", false);
		leechingTicks = input.getIntOr("LeechingTicks", 0);
		stabTicks = input.getIntOr("StabTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.storeNullable("LeechData", LeechData.CODEC, leechData);
		EntityReference.store(stuckEntity, output, "StuckEntity");
		output.putBoolean("HasStuck", hasStuck);
		output.putInt("LeechingTicks", leechingTicks);
		output.putInt("StabTicks", stabTicks);
	}

	@Override
	public void tick() {
		if (hasStuck) {
			LivingEntity stuckEntity = getStuckEntity();
			if (stuckEntity != null && stuckEntity.slib$exists()) {
				obj.snapTo(stuckEntity.getX(), stuckEntity.getEyeY(), stuckEntity.getZ());
				obj.setDeltaMovement(Vec3.ZERO);
				leechingTicks++;
				if (stabTicks > 0) {
					stabTicks--;
				}
			}
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (hasStuck) {
			LivingEntity stuckEntity = getStuckEntity();
			if (stuckEntity != null && stuckEntity.slib$exists()) {
				if (leechingTicks % 20 == 0) {
					int timeUntilRegen = stuckEntity.invulnerableTime;
					stuckEntity.invulnerableTime = 0;
					if (stuckEntity.hurtServer((ServerLevel) obj.level(), obj.level().damageSources().source(EnchancementDamageTypes.LIFE_DRAIN, obj, obj.getOwner()), leechData.damage()) && obj.getOwner() instanceof LivingEntity living && living.slib$exists()) {
						living.heal(leechData.healAmount());
					}
					stuckEntity.invulnerableTime = timeUntilRegen;
					stabTicks = 20;
					sync();
				}
				if (leechingTicks >= leechData.maxTicks()) {
					unleech();
				}
			} else {
				unleech();
			}
		}
	}

	@Override
	public void clientTick() {
		tick();
		LivingEntity stuckEntity = getStuckEntity();
		if (stuckEntity != null && stuckEntity.slib$exists() && stabTicks == 19) {
			SLibClientUtils.addParticles(stuckEntity, ParticleTypes.DAMAGE_INDICATOR, 5, ParticleAnchor.BODY);
		}
	}

	public void sync() {
		EnchancementEntityComponents.LEECHING_TRIDENT.sync(obj);
	}

	public boolean hasLeech() {
		return leechData != null;
	}

	public @Nullable LivingEntity getStuckEntity() {
		return EntityReference.getLivingEntity(stuckEntity, obj.level());
	}

	public void setStuckEntity(@Nullable LivingEntity stuckEntity) {
		this.stuckEntity = EntityReference.of(stuckEntity);
		hasStuck = stuckEntity != null;
	}

	public int getLeechingTicks() {
		return leechingTicks;
	}

	public int getStabTicks() {
		return stabTicks;
	}

	public void unleech() {
		setStuckEntity(null);
		leechData = null;
		leechingTicks = 0;
		stabTicks = 0;
		sync();
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, Entity entity) {
		if (entity instanceof ThrownTrident) {
			MutableFloat damage = new MutableFloat(), healAmount = new MutableFloat(), duration = new MutableFloat();
			if (EnchantmentHelper.has(stack, EnchancementEnchantmentEffectComponentTypes.LEECHING_TRIDENT)) {
				LeechingTridentEffect.setValues(user.getRandom(), damage, healAmount, duration, Collections.singleton(stack));
			} else if (!(user instanceof Player) && EnchancementUtil.hasAnyEnchantmentsWith(user, EnchancementEnchantmentEffectComponentTypes.LEECHING_TRIDENT)) {
				LeechingTridentEffect.setValues(user.getRandom(), damage, healAmount, duration, EnchancementUtil.getHeldItems(user));
			}
			if (damage.floatValue() != 0) {
				LeechingTridentComponent leechingTrident = EnchancementEntityComponents.LEECHING_TRIDENT.get(entity);
				leechingTrident.leechData = new LeechData(damage.floatValue(), healAmount.floatValue(), Mth.floor(duration.floatValue() * 20));
				leechingTrident.sync();
			}
		}
	}

	private record LeechData(float damage, float healAmount, int maxTicks) {
		private static final Codec<LeechData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.FLOAT.fieldOf("damage").forGetter(LeechData::damage),
				Codec.FLOAT.fieldOf("heal_amount").forGetter(LeechData::healAmount),
				Codec.INT.fieldOf("max_ticks").forGetter(LeechData::maxTicks)
		).apply(instance, LeechData::new));
	}
}
