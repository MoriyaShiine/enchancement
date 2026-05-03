/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.AmethystShard;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.IceShard;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.Torch;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jspecify.annotations.Nullable;

import java.util.*;

public record AllowLoadingProjectileEffect(Identifier model, SoundEvent soundEvent, Item projectileItem, boolean onlyAllow) {
	public static final Codec<AllowLoadingProjectileEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Identifier.CODEC.fieldOf("model").forGetter(AllowLoadingProjectileEffect::model),
					BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("sound_event").forGetter(AllowLoadingProjectileEffect::soundEvent),
					BuiltInRegistries.ITEM.byNameCodec().fieldOf("projectile_item").forGetter(AllowLoadingProjectileEffect::projectileItem),
					Codec.BOOL.fieldOf("only_allow").forGetter(AllowLoadingProjectileEffect::onlyAllow))
			.apply(instance, AllowLoadingProjectileEffect::new));

	public static final Map<Item, ProjectileBuilder> PROJECTILE_MAP = new HashMap<>();

	public static @Nullable SoundEvent cachedSoundEvent = null;

	static {
		PROJECTILE_MAP.put(Items.EGG, (level, mob, projectile, _) -> new ThrownEgg(level, mob, projectile));
		PROJECTILE_MAP.put(Items.ENDER_PEARL, (level, mob, projectile, _) -> new ThrownEnderpearl(level, mob, projectile));
		PROJECTILE_MAP.put(Items.EXPERIENCE_BOTTLE, (level, mob, projectile, _) -> new ThrownExperienceBottle(level, mob, projectile));
		PROJECTILE_MAP.put(Items.SNOWBALL, (level, mob, projectile, _) -> new Snowball(level, mob, projectile));
		PROJECTILE_MAP.put(Items.TRIDENT, (level, owner, projectile, _) -> new ThrownTrident(level, owner, projectile));

		PROJECTILE_MAP.put(Items.AMETHYST_SHARD, (level, mob, _, firedFromWeapon) -> {
			AmethystShard shard = new AmethystShard(level, mob, firedFromWeapon);
			shard.setBaseDamage(3);
			return shard;
		});
		PROJECTILE_MAP.put(Items.ICE, (level, mob, _, firedFromWeapon) -> {
			IceShard shard = new IceShard(level, mob, firedFromWeapon);
			shard.setBaseDamage(3);
			return shard;
		});
		PROJECTILE_MAP.put(Items.TORCH, (level, mob, projectile, firedFromWeapon) -> {
			Torch torch = new Torch(level, mob, projectile, firedFromWeapon);
			torch.setBaseDamage(torch.baseDamage / 3);
			int enchantmentLevel = 0;
			if (firedFromWeapon != null) {
				for (Holder<Enchantment> enchantment : firedFromWeapon.getEnchantments().keySet()) {
					if (EnchantmentHelper.has(firedFromWeapon, ModEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE)) {
						enchantmentLevel += EnchantmentHelper.getItemEnchantmentLevel(enchantment, firedFromWeapon);
					}
				}
			}
			torch.setIgnitionTime(Math.max(1, enchantmentLevel));
			return torch;
		});
	}

	public static @Nullable Identifier getModel(ItemStack stack, Item projectileItem) {
		final Identifier[] model = {null};
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, _) -> {
			List<ConditionalEffect<AllowLoadingProjectileEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE);
			if (effects != null) {
				effects.forEach(effect -> {
					if (effect.effect().projectileItem() == projectileItem) {
						model[0] = effect.effect().model();
					}
				});
			}
		});
		return model[0];
	}

	public static @Nullable SoundEvent getSoundEvent(ItemStack stack, Item projectile) {
		final SoundEvent[] soundEvent = {null};
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, _) -> {
			List<ConditionalEffect<AllowLoadingProjectileEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE);
			if (effects != null) {
				effects.forEach(effect -> {
					if (effect.effect().projectileItem() == projectile) {
						soundEvent[0] = effect.effect().soundEvent();
					}
				});
			}
		});
		return soundEvent[0];
	}

	public static Set<Item> getItems(ItemStack stack) {
		Set<Item> items = new HashSet<>();
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, _) -> {
			List<ConditionalEffect<AllowLoadingProjectileEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE);
			if (effects != null) {
				effects.forEach(effect -> items.add(effect.effect().projectileItem()));
			}
		});
		return items;
	}

	public static boolean onlyAllow(ItemStack weapon) {
		MutableBoolean value = new MutableBoolean();
		EnchantmentHelper.runIterationOnItem(weapon, (enchantment, _) -> {
			List<ConditionalEffect<AllowLoadingProjectileEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE);
			if (effects != null) {
				effects.forEach(effect -> value.setValue(value.booleanValue() || effect.effect().onlyAllow()));
			}
		});
		return value.booleanValue();
	}

	public interface ProjectileBuilder {
		Projectile getProjectile(Level level, LivingEntity mob, ItemStack projectile, @Nullable ItemStack firedFromWeapon);
	}
}
