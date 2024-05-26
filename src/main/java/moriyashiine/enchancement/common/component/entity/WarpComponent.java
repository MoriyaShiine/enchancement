/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;

public class WarpComponent implements AutoSyncedComponent, ClientTickingComponent {
	private final TridentEntity obj;
	private boolean hasWarp = false;

	public WarpComponent(TridentEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		hasWarp = tag.getBoolean("HasWarp");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("HasWarp", hasWarp);
	}

	@Override
	public void clientTick() {
		if (hasWarp) {
			for (int i = 0; i < 8; i++) {
				obj.getWorld().addParticle(ParticleTypes.REVERSE_PORTAL, obj.getParticleX(1), obj.getRandomBodyY(), obj.getParticleZ(1), 0, 0, 0);
			}
		}
	}

	public void sync() {
		ModEntityComponents.WARP.sync(obj);
	}

	public void setHasWarp(boolean hasWarp) {
		this.hasWarp = hasWarp;
	}

	public boolean hasWarp() {
		return hasWarp;
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, PersistentProjectileEntity trident) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.WARP, stack) || (user instanceof DrownedEntity && EnchancementUtil.hasEnchantment(ModEnchantments.WARP, user))) {
			WarpComponent warpComponent = ModEntityComponents.WARP.get(trident);
			warpComponent.setHasWarp(true);
			warpComponent.sync();
		}
	}
}
