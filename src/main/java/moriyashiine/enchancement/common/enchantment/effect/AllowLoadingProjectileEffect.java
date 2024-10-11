/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.entity.projectile.AmethystShardEntity;
import moriyashiine.enchancement.common.entity.projectile.TorchEntity;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record AllowLoadingProjectileEffect(Item projectileItem, boolean onlyAllow) {
	public static final Codec<AllowLoadingProjectileEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Registries.ITEM.getCodec().fieldOf("projectile_item").forGetter(AllowLoadingProjectileEffect::projectileItem),
					Codec.BOOL.fieldOf("only_allow").forGetter(AllowLoadingProjectileEffect::onlyAllow))
			.apply(instance, AllowLoadingProjectileEffect::new));

	public static final Map<Item, ProjectileBuilder> PROJECTILE_MAP = new HashMap<>();

	static {
		PROJECTILE_MAP.put(Items.EGG, (world, owner, stack, shotFrom) -> new EggEntity(world, owner));
		PROJECTILE_MAP.put(Items.ENDER_PEARL, (world, owner, stack, shotFrom) -> new EnderPearlEntity(world, owner));
		PROJECTILE_MAP.put(Items.EXPERIENCE_BOTTLE, (world, owner, stack, shotFrom) -> new ExperienceBottleEntity(world, owner));
		PROJECTILE_MAP.put(Items.SNOWBALL, (world, owner, stack, shotFrom) -> new SnowballEntity(world, owner));
		PROJECTILE_MAP.put(Items.TRIDENT, (world, owner, stack, shotFrom) -> new TridentEntity(world, owner, stack));

		PROJECTILE_MAP.put(Items.AMETHYST_SHARD, (world, owner, stack, shotFrom) -> new AmethystShardEntity(world, owner, shotFrom));
		PROJECTILE_MAP.put(Items.TORCH, (world, owner, stack, shotFrom) -> {
			TorchEntity torch = new TorchEntity(world, owner, stack, shotFrom);
			torch.setDamage(torch.getDamage() / 2);
			int level = 0;
			if (shotFrom != null) {
				for (RegistryEntry<Enchantment> enchantment : EnchantmentHelper.getEnchantments(shotFrom).getEnchantments()) {
					if (EnchantmentHelper.hasAnyEnchantmentsWith(shotFrom, ModEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE)) {
						level += EnchantmentHelper.getLevel(enchantment, shotFrom);
					}
				}
			}
			torch.setIgnitionTime(Math.max(1, level));
			return torch;
		});
	}

	public static Set<Item> getItems(ItemStack stack) {
		Set<Item> items = new HashSet<>();
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			List<EnchantmentEffectEntry<AllowLoadingProjectileEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE);
			if (effects != null) {
				effects.forEach(effect -> items.add(effect.effect().projectileItem()));
			}
		});
		return items;
	}

	public static boolean disable(ItemStack weaponStack, ItemStack projectileStack) {
		MutableBoolean value = new MutableBoolean();
		EnchantmentHelper.forEachEnchantment(weaponStack, (enchantment, level) -> {
			List<EnchantmentEffectEntry<AllowLoadingProjectileEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE);
			if (effects != null) {
				effects.forEach(effect -> value.setValue(value.booleanValue() || effect.effect().onlyAllow()));
			}
		});
		return value.booleanValue() && !getItems(weaponStack).contains(projectileStack.getItem());
	}

	public interface ProjectileBuilder {
		ProjectileEntity getProjectile(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom);
	}
}
