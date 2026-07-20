package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.client.payload.PlayEMeterFloatSoundPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.payload.EMeterC2SPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.EMeterEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.UUID;

public class EMeterComponent implements AutoSyncedComponent, CommonTickingComponent {
	private static final AttributeModifier SAFE_FALL_DISTANCE_MODIFIER = new AttributeModifier(Enchancement.id("e_meter_safe_fall_distance"), 6, AttributeModifier.Operation.ADD_VALUE);
	private static final AttributeModifier STEP_HEIGHT_MODIFIER = new AttributeModifier(Enchancement.id("e_meter_step_height"), 1, AttributeModifier.Operation.ADD_VALUE);

	private static final int MAX_METER_TICKS = 90;

	private final LivingEntity obj;
	private int meterTicks = 0;
	private boolean reachedMax = false, shouldFloat = false;

	private float speedMultiplier = 0;
	private boolean floating = false;
	private UUID floatingUuid = null;

	public EMeterComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		meterTicks = input.getIntOr("MeterTicks", 0);
		reachedMax = input.getBooleanOr("ReachedMax", false);
		shouldFloat = input.getBooleanOr("ShouldFloat", false);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putInt("MeterTicks", meterTicks);
		output.putBoolean("ReachedMax", reachedMax);
		output.putBoolean("ShouldFloat", shouldFloat);
	}

	@Override
	public void tick() {
		speedMultiplier = EMeterEffect.getSpeedMultiplier(obj);
		floating = false;
		if (hasEMeter()) {
			if (shouldFloat) {
				if (canFloat()) {
					if (obj.canSimulateMovement()) {
						float strength = getMeterProgress() * EMeterEffect.getFloatStrength(obj);
						obj.setDeltaMovement(obj.getDeltaMovement().x() * 0.98, Math.max(strength, obj.getDeltaMovement().y()), obj.getDeltaMovement().z() * 0.98);
					}
					floating = true;
					obj.gameEvent(GameEvent.ENTITY_ACTION);
					EnchancementUtil.resetFallDistance(obj);
				} else {
					shouldFloat = false;
				}
			}
			if (obj.onGround() && shouldMeterIncrease()) {
				if (meterTicks < MAX_METER_TICKS) {
					meterTicks = Math.min(MAX_METER_TICKS, meterTicks + 2);
				}
			} else if (meterTicks > 0) {
				int decrease = floating || obj.onGround() ? 2 : 1;
				if (obj.getKnownSpeed().horizontalDistanceSqr() == 0) {
					decrease *= 2;
				}
				meterTicks = Math.max(0, meterTicks - decrease);
			}
			if (obj.hurtTime > 0) {
				meterTicks = Math.max(0, meterTicks - 2);
			}
			if (meterTicks == MAX_METER_TICKS) {
				reachedMax = true;
			} else if (filledMeters() == 0) {
				reachedMax = false;
			}
			if (!floating) {
				floatingUuid = null;
			}
		} else {
			meterTicks = 0;
			reachedMax = shouldFloat = false;
		}
	}

	@Override
	public void serverTick() {
		boolean lastFloating = floating;
		tick();
		if (floating && !lastFloating) {
			if (floatingUuid == null) {
				floatingUuid = UUID.randomUUID();
			}
			PlayerLookup.tracking(obj).forEach(receiver -> PlayEMeterFloatSoundPayload.send(receiver, obj, floatingUuid));
			if (obj instanceof ServerPlayer player) {
				PlayEMeterFloatSoundPayload.send(player, player, floatingUuid);
			}
		}
		SLibUtils.applyAttributeModifier(obj, Attributes.SAFE_FALL_DISTANCE, SAFE_FALL_DISTANCE_MODIFIER, reachedMax());
		SLibUtils.applyAttributeModifier(obj, Attributes.STEP_HEIGHT, STEP_HEIGHT_MODIFIER, getSpeedBonus() > 0);
	}

	@Override
	public void clientTick() {
		tick();
		if (hasEMeter()) {
			LivingEntity controllingObj = obj.getControllingPassenger() instanceof Player player ? player : obj;
			if (!controllingObj.isSpectator() && SLibClientUtils.isHost(controllingObj)) {
				boolean lastShouldFloat = shouldFloat;
				if (EnchancementClient.E_METER_HOVER_KEYMAPPING.isDown()) {
					if (canFloat()) {
						shouldFloat = true;
					}
				} else if (shouldFloat) {
					shouldFloat = false;
				}
				if (shouldFloat != lastShouldFloat) {
					EMeterC2SPayload.send(obj, shouldFloat);
				}
			}
			if (reachedMax() && obj.isSprinting()) {
				SLibClientUtils.addParticles(obj, ParticleTypes.CRIT, 1, ParticleAnchor.CHEST);
			}
			if (shouldFloat) {
				for (int i = 0; i < (SLibClientUtils.shouldAddParticles(obj) ? 4 : 1); i++) {
					obj.level().addParticle(ParticleTypes.ELECTRIC_SPARK, obj.getRandomX(1), obj.getY() + obj.getBbHeight() / 2, obj.getRandomZ(1), 0, 0, 0);
				}
			}
		}
	}

	public boolean hasEMeter() {
		return speedMultiplier > 0;
	}

	public boolean reachedMax() {
		return reachedMax;
	}

	public void setShouldFloat(boolean shouldFloat) {
		this.shouldFloat = shouldFloat;
	}

	public float getSpeedBonus() {
		return getMeterProgress() * speedMultiplier;
	}

	public int filledMeters() {
		return (meterTicks + 1) / (MAX_METER_TICKS / 9);
	}

	public boolean canFloat() {
		return reachedMax() && shouldMeterIncrease() && SLibUtils.hasNormalMovement(obj) && !EnchancementEntityComponents.BOOST_IN_FLUID.get(obj).blocksAirEffects();
	}

	public boolean isFloating() {
		return floating;
	}

	public boolean isFloatingUuid(UUID floatingUuid) {
		return isFloating() && floatingUuid.equals(this.floatingUuid);
	}

	public void setFloatingUuid(UUID floatingUuid) {
		this.floatingUuid = floatingUuid;
	}

	private boolean shouldMeterIncrease() {
		if (!obj.slib$isPlayer() && obj.getKnownSpeed().horizontalDistanceSqr() > 0) {
			return true;
		}
		return obj.isSprinting() || EnchancementEntityComponents.SLIDE.get(obj).isSliding();
	}

	private float getMeterProgress() {
		return meterTicks / (float) MAX_METER_TICKS;
	}
}
