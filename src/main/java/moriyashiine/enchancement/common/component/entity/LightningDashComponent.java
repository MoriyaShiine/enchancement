/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.client.payload.AddLightningDashParticlesPayload;
import moriyashiine.enchancement.client.payload.UseLightningDashPayload;
import moriyashiine.enchancement.client.sound.SparkSoundInstance;
import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import moriyashiine.enchancement.common.enchantment.effect.LightningDashEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.particle.SparkParticleEffect;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.List;

public class LightningDashComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private boolean using = false;
	private double cachedHeight = 0;
	private int floatTicks = 0, smashTicks = 0;

	private boolean playedSound = false;
	private int ticksUsing = 0, hitEntityTicks = 0;

	public LightningDashComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		using = tag.getBoolean("Using");
		floatTicks = tag.getInt("FloatTicks");
		smashTicks = tag.getInt("SmashTicks");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("Using", using);
		tag.putInt("FloatTicks", floatTicks);
		tag.putInt("SmashTicks", smashTicks);
	}

	@Override
	public void tick() {
		int chargeTime = LightningDashEffect.getChargeTime(obj.getRandom(), obj.getMainHandStack());
		if (isFloating() || isSmashing()) {
			if (chargeTime == 0) {
				cancel();
			}
		}
		if (isFloating()) {
			floatTicks--;
			obj.fallDistance = 0;
			obj.setVelocity(obj.getVelocity().multiply(0.9));
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
			if (!getNearby(3).isEmpty()) {
				hitEntityTicks = 2;
			}
			if (obj.isOnGround()) {
				if (smashTicks > 1) {
					smashTicks = 1;
				}
				if (smashTicks == 1) {
					obj.playSound(SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY, 1, 1);
					obj.getWorld().emitGameEvent(GameEvent.STEP, obj.getPos(), GameEvent.Emitter.of(obj.getSteppingBlockState()));
				} else {
					obj.fallDistance = 0;
				}
			}
		}
		if (chargeTime > 0 && ItemStack.areEqual(obj.getActiveItem(), obj.getMainHandStack())) {
			if (!playedSound && obj.getItemUseTime() == chargeTime) {
				obj.playSound(ModSoundEvents.ENTITY_GENERIC_PING, 1, 1);
				playedSound = true;
			}
			if (ticksUsing % 18 == 0) {
				obj.playSound(ModSoundEvents.ITEM_GENERIC_WHOOSH, 0.5F, 1);
			}
			ticksUsing++;
		} else {
			playedSound = false;
			ticksUsing = 0;
		}
		if (hitEntityTicks > 0) {
			hitEntityTicks--;
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (isFloating() && !EnchancementUtil.isSufficientlyHigh(obj, 0.25)) {
			cancel();
			sync();
		}
		if (smashTicks == 1 && obj.isOnGround()) {
			ServerWorld serverWorld = (ServerWorld) obj.getWorld();
			PlayerLookup.tracking(obj).forEach(foundPlayer -> AddLightningDashParticlesPayload.send(foundPlayer, obj.getId()));
			obj.fallDistance = (float) Math.max(0, cachedHeight - obj.getY());
			float base = (float) obj.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
			getNearby(1).forEach(entity -> {
				DamageSource source = obj instanceof PlayerEntity player ? entity.getDamageSources().playerAttack(player) : entity.getDamageSources().mobAttack(obj);
				float damage = EnchantmentHelper.getDamage(serverWorld, obj.getMainHandStack(), entity, source, base)
						+ obj.getMainHandStack().getItem().getBonusAttackDamage(entity, base, source);
				if (entity.damage(serverWorld, source, damage * LightningDashEffect.getSmashDamageMultiplier(obj.getRandom(), obj.getMainHandStack()))) {
					entity.takeKnockback(1.5, obj.getX() - entity.getX(), obj.getZ() - entity.getZ());
				}
			});
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (isFloating() && EnchancementClientUtil.shouldAddParticles(obj)) {
			for (int i = 0; i <= 4; i++) {
				obj.getWorld().addParticle(new SparkParticleEffect(obj.getPos().addRandom(obj.getRandom(), 1)), obj.getParticleX(1), obj.getRandomBodyY(), obj.getParticleZ(1), 0, 0, 0);
			}
		}
		if (smashTicks == 1 && obj.isOnGround()) {
			AddLightningDashParticlesPayload.addParticles(obj);
		}
	}

	public void sync() {
		ModEntityComponents.LIGHTNING_DASH.sync(obj);
	}

	public void setUsing(boolean using) {
		this.using = using;
	}

	public boolean isUsing() {
		return using;
	}

	public void useCommon(Vec3d lungeVelocity, int floatTicks) {
		obj.setVelocity(lungeVelocity);
		this.floatTicks = floatTicks;
	}

	@Environment(EnvType.CLIENT)
	public void useClient() {
		MinecraftClient.getInstance().getSoundManager().play(new SparkSoundInstance(obj));
	}

	public void useServer(Vec3d lungeVelocity, int floatTicks) {
		PlayerLookup.tracking(obj).forEach(foundPlayer -> UseLightningDashPayload.send(foundPlayer, obj.getId(), lungeVelocity, floatTicks));
	}

	public boolean isFloating() {
		return floatTicks > 0;
	}

	private boolean isSmashing() {
		return smashTicks > 0;
	}

	public boolean hitNoEntity() {
		return hitEntityTicks == 0;
	}

	public void cancel() {
		floatTicks = smashTicks = 0;
	}

	private List<LivingEntity> getNearby(int range) {
		return obj.getWorld().getEntitiesByClass(LivingEntity.class, new Box(obj.getBlockPos()).expand(2, range, 2), foundEntity ->
				foundEntity.isAlive() && foundEntity.distanceTo(obj) < 10 && EnchancementUtil.shouldHurt(obj, foundEntity) && EnchancementUtil.canSee(obj, foundEntity, range));
	}
}
