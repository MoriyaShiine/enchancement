/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.enchantment.effect.TeleportOnHitEffect;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;

import java.util.Collections;

public class TeleportOnHitComponent implements AutoSyncedComponent, ClientTickingComponent {
	private final ProjectileEntity obj;
	private boolean teleportsOnBlockHit = false, teleportsOnEntityHit = false;

	public TeleportOnHitComponent(ProjectileEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		teleportsOnBlockHit = tag.getBoolean("TeleportsOnBlockHit", false);
		teleportsOnEntityHit = tag.getBoolean("TeleportsOnEntityHit", false);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("TeleportsOnBlockHit", teleportsOnBlockHit);
		tag.putBoolean("TeleportsOnEntityHit", teleportsOnEntityHit);
	}

	@Override
	public void clientTick() {
		if (teleportsOnBlockHit() || teleportsOnEntityHit()) {
			SLibClientUtils.addParticles(obj, ParticleTypes.REVERSE_PORTAL, 8, ParticleAnchor.BODY);
		}
	}

	public void sync() {
		ModEntityComponents.TELEPORT_ON_HIT.sync(obj);
	}

	public void disable() {
		teleportsOnBlockHit = teleportsOnEntityHit = false;
		if (obj instanceof PersistentProjectileEntity persistentProjectile && obj.getOwner() instanceof PlayerEntity player && !player.isCreative() && player.giveItemStack(persistentProjectile.asItemStack())) {
			obj.discard();
		}
	}

	public boolean teleportsOnBlockHit() {
		return teleportsOnBlockHit;
	}

	public boolean teleportsOnEntityHit() {
		return teleportsOnEntityHit;
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, Entity entity) {
		if (entity instanceof ProjectileEntity) {
			MutableBoolean teleportsOnBlockHit = new MutableBoolean(), teleportsOnEntityHit = new MutableBoolean();
			if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.TELEPORT_ON_HIT)) {
				TeleportOnHitEffect.setValues(teleportsOnBlockHit, teleportsOnEntityHit, Collections.singleton(stack));
			} else if (!(user instanceof PlayerEntity) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.TELEPORT_ON_HIT)) {
				TeleportOnHitEffect.setValues(teleportsOnBlockHit, teleportsOnEntityHit, EnchancementUtil.getHeldItems(user));
			}
			if (teleportsOnBlockHit.booleanValue() || teleportsOnEntityHit.booleanValue()) {
				TeleportOnHitComponent teleportOnHitComponent = ModEntityComponents.TELEPORT_ON_HIT.get(entity);
				teleportOnHitComponent.teleportsOnBlockHit = teleportsOnBlockHit.booleanValue();
				teleportOnHitComponent.teleportsOnEntityHit = teleportsOnEntityHit.booleanValue();
				teleportOnHitComponent.sync();
			}
		}
	}
}
