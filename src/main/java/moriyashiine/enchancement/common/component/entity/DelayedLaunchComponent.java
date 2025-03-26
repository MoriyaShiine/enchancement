/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.enchantment.effect.DelayedLaunchEffect;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.Collections;

public class DelayedLaunchComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PersistentProjectileEntity obj;
	private ItemStack stackShotFrom = null;
	private int maxDuration = 0, peakDuration = 0;
	private float maxMultiplier = 0;
	private boolean allowRedirect = false;

	private Vec3d storedVelocity = null;
	private float forcedPitch = 0, forcedYaw = 0;
	private float cachedSpeed = 0, cachedDivergence = 0;
	private int ticksFloating = 0;

	public DelayedLaunchComponent(PersistentProjectileEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		storedVelocity = tag.get("StoredVelocity", Vec3d.CODEC).orElse(null);
		maxDuration = tag.getInt("MaxDuration", 0);
		peakDuration = tag.getInt("PeakDuration", 0);
		maxMultiplier = tag.getFloat("MaxMultiplier", 0);
		allowRedirect = tag.getBoolean("AllowRedirect", false);

		ticksFloating = tag.getInt("TicksFloating", 0);
		forcedPitch = tag.getFloat("ForcedPitch", 0);
		forcedYaw = tag.getFloat("ForcedYaw", 0);
		cachedSpeed = tag.getFloat("CachedSpeed", 0);
		cachedDivergence = tag.getFloat("CachedDivergence", 0);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		if (storedVelocity != null) {
			tag.put("StoredVelocity", Vec3d.CODEC, storedVelocity);
		}
		tag.putInt("MaxDuration", maxDuration);
		tag.putInt("PeakDuration", peakDuration);
		tag.putFloat("MaxMultiplier", maxMultiplier);
		tag.putBoolean("AllowRedirect", allowRedirect);

		tag.putInt("TicksFloating", ticksFloating);
		tag.putFloat("ForcedPitch", forcedPitch);
		tag.putFloat("ForcedYaw", forcedYaw);
		tag.putFloat("CachedSpeed", cachedSpeed);
		tag.putFloat("CachedDivergence", cachedDivergence);
	}

	@Override
	public void tick() {
		if (isEnabled()) {
			obj.setVelocity(Vec3d.ZERO);
			obj.setPitch(forcedPitch);
			obj.setYaw(forcedYaw);
			ticksFloating++;
		}
	}

	@Override
	public void serverTick() {
		if (isEnabled()) {
			if (storedVelocity == null) {
				storedVelocity = obj.getVelocity();
				forcedPitch = obj.getPitch();
				forcedYaw = obj.getYaw();
				sync();
			}
			boolean punching = obj.getOwner() instanceof LivingEntity living && living.handSwinging && (living.getMainHandStack() == stackShotFrom || living.getOffHandStack() == stackShotFrom);
			if (ticksFloating > maxDuration || punching) {
				if (allowRedirect && punching && obj.getOwner() instanceof LivingEntity living && living.isSneaking()) {
					HitResult result = ProjectileUtil.getCollision(living, entity -> !entity.isSpectator() && entity.canHit(), 64);
					Vec3d pos;
					if (result instanceof EntityHitResult entityHitResult) {
						pos = entityHitResult.getEntity().getEyePos();
					} else {
						pos = result.getPos();
					}
					obj.setVelocity(pos.getX() - obj.getX(), pos.getY() - obj.getY(), pos.getZ() - obj.getZ(), cachedSpeed, cachedDivergence);
					storedVelocity = obj.getVelocity();
					obj.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, pos);
					forcedPitch = MathHelper.wrapDegrees(obj.getPitch() + 180);
					forcedYaw = MathHelper.wrapDegrees(-(obj.getYaw() + 180));
				}
				obj.setDamage(obj.damage * MathHelper.lerp(Math.min(1, (float) ticksFloating / peakDuration), 1, 1 + maxMultiplier));
				obj.setVelocity(storedVelocity);
				maxDuration = peakDuration = 0;
				maxMultiplier = 0;
				allowRedirect = false;
				sync();
			}
		}
		tick();
	}

	public void sync() {
		ModEntityComponents.DELAYED_LAUNCH.sync(obj);
	}

	public boolean isEnabled() {
		return maxDuration > 0;
	}

	public boolean alwaysHurt() {
		return storedVelocity != null;
	}

	public boolean shouldChangeParticles() {
		return peakDuration > 0 && ticksFloating >= peakDuration;
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, Entity entity, float speed, float divergence) {
		if (entity instanceof PersistentProjectileEntity projectile) {
			MutableFloat maxDuration = new MutableFloat(), peakDuration = new MutableFloat(), maxMultiplier = new MutableFloat();
			MutableBoolean allowRedirect = new MutableBoolean();
			if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.DELAYED_LAUNCH)) {
				DelayedLaunchEffect.setValues(user.getRandom(), maxDuration, peakDuration, maxMultiplier, allowRedirect, Collections.singleton(stack));
			} else if (!(user instanceof PlayerEntity) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.DELAYED_LAUNCH)) {
				DelayedLaunchEffect.setValues(user.getRandom(), maxDuration, peakDuration, maxMultiplier, allowRedirect, EnchancementUtil.getHeldItems(user));
			}
			if (maxDuration.floatValue() != 0) {
				DelayedLaunchComponent delayedLaunchComponent = ModEntityComponents.DELAYED_LAUNCH.get(entity);
				delayedLaunchComponent.stackShotFrom = stack;
				delayedLaunchComponent.maxDuration = MathHelper.floor(maxDuration.floatValue() * 20);
				delayedLaunchComponent.peakDuration = MathHelper.floor(peakDuration.floatValue() * 20);
				delayedLaunchComponent.maxMultiplier = maxMultiplier.floatValue();
				delayedLaunchComponent.allowRedirect = allowRedirect.booleanValue();

				delayedLaunchComponent.cachedSpeed = speed;
				delayedLaunchComponent.cachedDivergence = divergence;

				delayedLaunchComponent.sync();
				projectile.setCritical(true);
			}
		}
	}
}
