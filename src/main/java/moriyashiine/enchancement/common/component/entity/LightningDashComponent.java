/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.client.payload.PlaySparkSoundPayload;
import moriyashiine.enchancement.common.enchantment.effect.LightningDashEffect;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.particle.SparkParticleEffect;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.event.GameEvent;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.List;

public class LightningDashComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private boolean using = false;
	private double cachedHeight = 0;
	private int floatTicks = 0, smashTicks = 0;

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
				floatTicks = smashTicks = 0;
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
			if (!EnchancementUtil.isSufficientlyHigh(obj, 0.25)) {
				floatTicks = smashTicks = 0;
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
		if (chargeTime > 0 && obj.isUsingItem()) {
			if (ticksUsing == MathHelper.floor(chargeTime * EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MULTIPLY_CHARGE_TIME, obj.getRandom(), obj.getActiveItem(), 1)) + 3) {
				obj.playSound(ModSoundEvents.ENTITY_GENERIC_PING, 1, 1);
			}
			if (ticksUsing % 18 == 0) {
				obj.playSound(ModSoundEvents.ITEM_GENERIC_WHOOSH, 0.5F, 1);
			}
			ticksUsing++;
		} else {
			ticksUsing = 0;
		}
		if (hitEntityTicks > 0) {
			hitEntityTicks--;
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (smashTicks == 1 && obj.isOnGround()) {
			obj.fallDistance = (float) Math.max(0, cachedHeight - obj.getY());
			float base = (float) obj.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
			getNearby(1).forEach(entity -> {
				DamageSource source = obj instanceof PlayerEntity player ? entity.getDamageSources().playerAttack(player) : entity.getDamageSources().mobAttack(obj);
				float damage = EnchantmentHelper.getDamage((ServerWorld) obj.getWorld(), obj.getMainHandStack(), entity, source, base)
						+ obj.getMainHandStack().getItem().getBonusAttackDamage(entity, base, source);
				if (entity.damage(source, damage * LightningDashEffect.getSmashDamageMultiplier(obj.getRandom(), obj.getMainHandStack()))) {
					entity.takeKnockback(1.5, obj.getX() - entity.getX(), obj.getZ() - entity.getZ());
				}
			});
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (isFloating()) {
			if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || obj != MinecraftClient.getInstance().cameraEntity) {
				for (int i = 0; i <= 4; i++) {
					obj.getWorld().addParticle(new SparkParticleEffect(obj.getPos().addRandom(obj.getRandom(), 1)), obj.getParticleX(1), obj.getRandomBodyY(), obj.getParticleZ(1), 0, 0, 0);
				}
			}
		}
		if (smashTicks == 1 && obj.isOnGround()) {
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			for (int i = 0; i < 360; i += 15) {
				for (int j = 1; j < 4; j++) {
					double x = obj.getX() + MathHelper.sin(i) * j / 2, z = obj.getZ() + MathHelper.cos(i) * j / 2;
					BlockState state = obj.getWorld().getBlockState(mutable.set(x, Math.round(obj.getY() - 1), z));
					if (!state.isReplaceable() && obj.getWorld().getBlockState(mutable.move(Direction.UP)).isReplaceable()) {
						BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, state);
						for (int k = 0; k < 8; k++) {
							obj.getWorld().addParticle(particle, x, mutable.getY() + 0.5, z, 0, 0, 0);
						}
					}
				}
			}
		}
	}

	public void setUsing(boolean using) {
		this.using = using;
	}

	public boolean isUsing() {
		return using;
	}

	public void startFloating(int floatTicks) {
		this.floatTicks = floatTicks;
		if (obj instanceof ServerPlayerEntity player) {
			PlayerLookup.tracking(obj).forEach(foundPlayer -> PlaySparkSoundPayload.send(foundPlayer, obj.getId()));
			PlaySparkSoundPayload.send(player, obj.getId());
		}
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

	private List<LivingEntity> getNearby(int range) {
		return obj.getWorld().getEntitiesByClass(LivingEntity.class, new Box(obj.getBlockPos()).expand(2, range, 2), foundEntity ->
				foundEntity.isAlive() && foundEntity.distanceTo(obj) < 10 && EnchancementUtil.shouldHurt(obj, foundEntity) && EnchancementUtil.canSee(obj, foundEntity, range));
	}
}
