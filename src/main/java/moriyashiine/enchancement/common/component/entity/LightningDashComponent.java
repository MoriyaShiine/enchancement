/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.client.payload.AddLightningDashParticlesPayload;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.particle.SparkParticleOption;
import moriyashiine.enchancement.common.world.item.effects.LightningDashEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.List;

public class LightningDashComponent extends UsingMaceComponent implements CommonTickingComponent {
	private final Player obj;
	private double cachedHeight = 0;
	private int floatTicks = 0, smashTicks = 0;

	private int ticksUsing = 0;

	private double nextTickFallDistance = 0;

	public LightningDashComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		super.readData(input);
		floatTicks = input.getIntOr("FloatTicks", 0);
		smashTicks = input.getIntOr("SmashTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		super.writeData(output);
		output.putInt("FloatTicks", floatTicks);
		output.putInt("SmashTicks", smashTicks);
	}

	@Override
	public void tick() {
		boolean hasLightningDash = LightningDashEffect.getFloatTime(obj.getRandom(), obj.getMainHandItem()) != 0;
		if (isFloating() || isSmashing()) {
			if (!hasLightningDash) {
				cancel();
			}
		}
		if (isFloating()) {
			floatTicks--;
			obj.resetFallDistance();
			obj.setDeltaMovement(obj.getDeltaMovement().scale(0.9));
			obj.gameEvent(GameEvent.ENTITY_ACTION);
			if (obj.swinging && obj.getXRot() > -15) {
				cachedHeight = obj.getY();
				smashTicks = 30;
				floatTicks = 0;
				obj.setDeltaMovement(obj.getLookAngle().scale(LightningDashEffect.getSmashStrength(obj.getRandom(), obj.getMainHandItem())));
				obj.playSound(ModSoundEvents.ENTITY_GENERIC_ZAP, 2, 1);
			}
		}
		if (isSmashing()) {
			smashTicks--;
			if (obj.onGround()) {
				if (smashTicks > 1) {
					smashTicks = 1;
				}
				if (smashTicks == 1) {
					obj.playSound(SoundEvents.MACE_SMASH_GROUND_HEAVY, 1, 1);
				}
			}
		}
		if (hasLightningDash && ItemStack.matches(obj.getUseItem(), obj.getMainHandItem())) {
			if (ticksUsing % 18 == 0) {
				obj.playSound(ModSoundEvents.ITEM_GENERIC_WHOOSH, 0.5F, 1);
			}
			ticksUsing++;
		} else {
			ticksUsing = 0;
		}
		if (nextTickFallDistance != 0) {
			obj.causeFallDamage(nextTickFallDistance, 1, obj.damageSources().fall());
			nextTickFallDistance = 0;
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (isFloating() && !SLibUtils.isSufficientlyHigh(obj, 0.25)) {
			cancel();
			sync();
		}
		if (smashTicks == 1 && obj.onGround()) {
			ServerLevel level = (ServerLevel) obj.level();
			PlayerLookup.tracking(obj).forEach(foundPlayer -> AddLightningDashParticlesPayload.send(foundPlayer, obj));
			obj.fallDistance = (float) Math.max(0, cachedHeight - obj.getY());
			float base = (float) obj.getAttributeValue(Attributes.ATTACK_DAMAGE);
			boolean[] hurt = {true};
			getNearby(3).forEach(entity -> {
				DamageSource source = obj instanceof Player player ? entity.damageSources().playerAttack(player) : entity.damageSources().mobAttack(obj);
				float damage = EnchantmentHelper.modifyDamage(level, obj.getMainHandItem(), entity, source, base) + obj.getMainHandItem().getItem().getAttackDamageBonus(entity, base, source);
				if (entity.hurtServer(level, source, damage * LightningDashEffect.getSmashDamageMultiplier(obj.getRandom(), obj.getMainHandItem()))) {
					entity.knockback(1.5, obj.getX() - entity.getX(), obj.getZ() - entity.getZ());
					hurt[0] = false;
				}
			});
			if (hurt[0] && !obj.isInWater()) {
				nextTickFallDistance = obj.fallDistance;
			}
			obj.fallDistance = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (isFloating() && SLibClientUtils.shouldAddParticles(obj)) {
			for (int i = 0; i <= 4; i++) {
				obj.level().addParticle(new SparkParticleOption(obj.position().offsetRandom(obj.getRandom(), 1)), obj.getRandomX(1), obj.getRandomY(), obj.getRandomZ(1), 0, 0, 0);
			}
		}
		if (smashTicks == 1 && obj.onGround()) {
			AddLightningDashParticlesPayload.addParticles(obj);
		}
	}

	public void sync() {
		ModEntityComponents.LIGHTNING_DASH.sync(obj);
	}

	public void setFloatTicks(int floatTicks) {
		this.floatTicks = floatTicks;
	}

	public boolean isFloating() {
		return floatTicks > 0;
	}

	public boolean isSmashing() {
		return smashTicks > 0;
	}

	public void cancel() {
		floatTicks = smashTicks = 0;
	}

	@SuppressWarnings("SameParameterValue")
	private List<LivingEntity> getNearby(int range) {
		return obj.level().getEntitiesOfClass(LivingEntity.class,
				new AABB(
						obj.getX() - 0.5 - range, obj.getY() - 1.5, obj.getZ() - 0.5 - range,
						obj.getX() + 0.5 + range, obj.getY() + 0.5 + range, obj.getZ() + 0.5 + range
				), foundEntity -> foundEntity.isAlive() && foundEntity.distanceTo(obj) < 10 && SLibUtils.shouldHurt(obj, foundEntity) && SLibUtils.hasLineOfSight(obj, foundEntity, range));
	}
}
