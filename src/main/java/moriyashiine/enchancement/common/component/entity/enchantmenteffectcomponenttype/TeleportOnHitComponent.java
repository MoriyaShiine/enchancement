/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.TeleportOnHitEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;

import java.util.Collections;

public class TeleportOnHitComponent implements AutoSyncedComponent, ClientTickingComponent {
	private final AbstractArrow obj;
	private boolean teleportsOnBlockHit = false, teleportsOnEntityHit = false;

	public TeleportOnHitComponent(AbstractArrow obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		teleportsOnBlockHit = input.getBooleanOr("TeleportsOnBlockHit", false);
		teleportsOnEntityHit = input.getBooleanOr("TeleportsOnEntityHit", false);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("TeleportsOnBlockHit", teleportsOnBlockHit);
		output.putBoolean("TeleportsOnEntityHit", teleportsOnEntityHit);
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
		if (obj.getOwner() instanceof Player player && !player.isCreative()) {
			if (EnchancementUtil.insertToCorrectTridentSlot(obj, player.getInventory(), obj.getPickupItem()) || player.addItem(obj.getPickupItem())) {
				obj.discard();
			}
		}
	}

	public boolean teleportsOnBlockHit() {
		return teleportsOnBlockHit;
	}

	public boolean teleportsOnEntityHit() {
		return teleportsOnEntityHit;
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, Entity entity) {
		if (entity instanceof AbstractArrow) {
			MutableBoolean teleportsOnBlockHit = new MutableBoolean(), teleportsOnEntityHit = new MutableBoolean();
			if (EnchantmentHelper.has(stack, ModEnchantmentEffectComponentTypes.TELEPORT_ON_HIT)) {
				TeleportOnHitEffect.setValues(teleportsOnBlockHit, teleportsOnEntityHit, Collections.singleton(stack));
			} else if (!(user instanceof Player) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.TELEPORT_ON_HIT)) {
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
