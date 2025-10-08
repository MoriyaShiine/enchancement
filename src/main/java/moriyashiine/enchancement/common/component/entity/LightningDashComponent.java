/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.client.payload.AddLightningDashParticlesPayload;
import moriyashiine.enchancement.common.enchantment.effect.LightningDashEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.particle.SparkParticleEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.Box;
import net.minecraft.world.event.GameEvent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.List;

public class LightningDashComponent extends UsingMaceComponent implements CommonTickingComponent {
	private final PlayerEntity obj;
	private double cachedHeight = 0;
	private int floatTicks = 0, smashTicks = 0;

	private int ticksUsing = 0;

	private double nextTickFallDistance = 0;

	public LightningDashComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		super.readData(readView);
		floatTicks = readView.getInt("FloatTicks", 0);
		smashTicks = readView.getInt("SmashTicks", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		super.writeData(writeView);
		writeView.putInt("FloatTicks", floatTicks);
		writeView.putInt("SmashTicks", smashTicks);
	}

	@Override
	public void tick() {
		boolean hasLightningDash = LightningDashEffect.getFloatTime(obj.getRandom(), obj.getMainHandStack()) != 0;
		if (isFloating() || isSmashing()) {
			if (!hasLightningDash) {
				cancel();
			}
		}
		if (isFloating()) {
			floatTicks--;
			obj.onLanding();
			obj.setVelocity(obj.getVelocity().multiply(0.9));
			obj.emitGameEvent(GameEvent.ENTITY_ACTION);
			if (obj.handSwinging && obj.getPitch() > -15) {
				cachedHeight = obj.getY();
				smashTicks = 30;
				floatTicks = 0;
				obj.setVelocity(obj.getRotationVector().multiply(LightningDashEffect.getSmashStrength(obj.getRandom(), obj.getMainHandStack())));
				obj.playSound(ModSoundEvents.ENTITY_GENERIC_ZAP, 2, 1);
			}
		}
		if (isSmashing()) {
			smashTicks--;
			if (obj.isOnGround()) {
				if (smashTicks > 1) {
					smashTicks = 1;
				}
				if (smashTicks == 1) {
					obj.playSound(SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY, 1, 1);
				}
			}
		}
		if (hasLightningDash && ItemStack.areEqual(obj.getActiveItem(), obj.getMainHandStack())) {
			if (ticksUsing % 18 == 0) {
				obj.playSound(ModSoundEvents.ITEM_GENERIC_WHOOSH, 0.5F, 1);
			}
			ticksUsing++;
		} else {
			ticksUsing = 0;
		}
		if (nextTickFallDistance != 0) {
			obj.handleFallDamage(nextTickFallDistance, 1, obj.getDamageSources().fall());
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
		if (smashTicks == 1 && obj.isOnGround()) {
			ServerWorld world = (ServerWorld) obj.getEntityWorld();
			PlayerLookup.tracking(obj).forEach(foundPlayer -> AddLightningDashParticlesPayload.send(foundPlayer, obj));
			obj.fallDistance = (float) Math.max(0, cachedHeight - obj.getY());
			float base = (float) obj.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
			boolean[] hurt = {true};
			getNearby(3).forEach(entity -> {
				DamageSource source = obj instanceof PlayerEntity player ? entity.getDamageSources().playerAttack(player) : entity.getDamageSources().mobAttack(obj);
				float damage = EnchantmentHelper.getDamage(world, obj.getMainHandStack(), entity, source, base)
						+ obj.getMainHandStack().getItem().getBonusAttackDamage(entity, base, source);
				if (entity.damage(world, source, damage * LightningDashEffect.getSmashDamageMultiplier(obj.getRandom(), obj.getMainHandStack()))) {
					entity.takeKnockback(1.5, obj.getX() - entity.getX(), obj.getZ() - entity.getZ());
					hurt[0] = false;
				}
			});
			if (hurt[0] && !obj.isTouchingWater()) {
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
				obj.getEntityWorld().addParticleClient(new SparkParticleEffect(obj.getEntityPos().addRandom(obj.getRandom(), 1)), obj.getParticleX(1), obj.getRandomBodyY(), obj.getParticleZ(1), 0, 0, 0);
			}
		}
		if (smashTicks == 1 && obj.isOnGround()) {
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
		return obj.getEntityWorld().getEntitiesByClass(LivingEntity.class,
				new Box(
						obj.getX() - 0.5 - range, obj.getY() - 1.5, obj.getZ() - 0.5 - range,
						obj.getX() + 0.5 + range, obj.getY() + 0.5 + range, obj.getZ() + 0.5 + range
				), foundEntity -> foundEntity.isAlive() && foundEntity.distanceTo(obj) < 10 && SLibUtils.shouldHurt(obj, foundEntity) && SLibUtils.canSee(obj, foundEntity, range));
	}
}
