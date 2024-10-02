/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.event.InitializeDefaultEnchantmentsEvent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import moriyashiine.enchancement.common.tag.ModItemTags;
import moriyashiine.enchancement.mixin.util.accessor.ItemEntityAccessor;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnchancementUtil {
	public static Registry<Enchantment> ENCHANTMENT_REGISTRY = null;

	public static final Object2IntMap<Enchantment> ORIGINAL_MAX_LEVELS = new Object2IntOpenHashMap<>();

	public static boolean shouldCancelTargetDamagedEnchantments = false;

	public static List<ItemEntity> mergeItemEntities(List<ItemEntity> drops) {
		for (int i = drops.size() - 1; i >= 0; i--) {
			if (i < drops.size() - 1) {
				ItemEntity itemEntity = drops.get(i);
				ItemEntity other = drops.get(i + 1);
				((ItemEntityAccessor) itemEntity).enchancement$tryMerge(other);
				if (itemEntity.getStack().isEmpty()) {
					drops.remove(i);
				}
				if (other.getStack().isEmpty()) {
					drops.remove(i + 1);
				}
			}
		}
		return drops;
	}

	public static String getTranslationKey(RegistryEntry<Enchantment> enchantment) {
		if (enchantment.value().description().getContent() instanceof TranslatableTextContent translatable) {
			return translatable.getKey();
		}
		throw new IllegalArgumentException("Enchantment " + enchantment.value().description().getString() + " does not have a translation key");
	}

	public static List<RegistryEntry.Reference<Enchantment>> getAllEnchantments() {
		return ENCHANTMENT_REGISTRY.streamEntries().toList();
	}

	@Nullable
	public static RegistryEntry<Enchantment> getRandomEnchantment(ItemStack stack, Random random) {
		List<RegistryEntry<Enchantment>> enchantments = new ArrayList<>();
		for (RegistryEntry<Enchantment> enchantment : getAllEnchantments()) {
			if (stack.canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE)) {
				enchantments.add(enchantment);
			}
		}
		if (!enchantments.isEmpty()) {
			return enchantments.get(random.nextInt(enchantments.size()));
		}
		return null;
	}

	@Nullable
	public static RegistryEntry<Enchantment> getReplacement(RegistryEntry<Enchantment> enchantment, ItemStack stack) {
		if (enchantment.getKey().isEmpty()) {
			return null;
		}
		List<RegistryEntry<Enchantment>> enchantments = new ArrayList<>();
		for (RegistryEntry<Enchantment> entry : getAllEnchantments()) {
			if (isEnchantmentAllowed(entry)) {
				if (stack.isOf(Items.ENCHANTED_BOOK) || stack.canBeEnchantedWith(entry, EnchantingContext.ACCEPTABLE)) {
					enchantments.add(entry);
				}
			}
		}
		if (enchantments.isEmpty()) {
			return null;
		}
		int index = (enchantment.getKey().get().getValue().hashCode() + Registries.ITEM.getId(stack.getItem()).hashCode()) % enchantments.size();
		if (index < 0) {
			index += enchantments.size();
		}
		return enchantments.get(index);
	}

	public static boolean hasWeakEnchantments(ItemStack stack) {
		if (stack.isIn(ModItemTags.STRONGLY_ENCHANTED)) {
			return false;
		}
		if (stack.isIn(ModItemTags.WEAKLY_ENCHANTED)) {
			return true;
		}
		if (stack.getItem() instanceof ArmorItem armorItem) {
			ArmorMaterial material = armorItem.getMaterial().value();
			for (ArmorMaterial mat : Registries.ARMOR_MATERIAL) {
				if (material == mat) {
					return mat == ArmorMaterials.LEATHER.value() || mat == ArmorMaterials.IRON.value();
				}
			}
			return material.enchantability() <= ArmorMaterials.IRON.value().enchantability();
		} else if (stack.getItem() instanceof ToolItem toolItem) {
			ToolMaterial material = toolItem.getMaterial();
			for (ToolMaterial mat : ToolMaterials.values()) {
				if (material == mat) {
					return mat == ToolMaterials.WOOD || mat == ToolMaterials.STONE || mat == ToolMaterials.IRON;
				}
			}
			return material.getEnchantability() <= ToolMaterials.IRON.getEnchantability();
		}
		return false;
	}

	public static boolean isEnchantmentAllowed(RegistryEntry<Enchantment> enchantment) {
		if (enchantment.getKey().isPresent()) {
			return isEnchantmentAllowed(enchantment.getKey().get().getValue());
		}
		return false;
	}

	public static boolean isEnchantmentAllowed(Identifier identifier) {
		if (identifier.equals(ModEnchantments.EMPTY_KEY.getValue())) {
			return false;
		}
		if (ModConfig.invertedList) {
			return ModConfig.disallowedEnchantments.contains(identifier.toString());
		}
		return !ModConfig.disallowedEnchantments.contains(identifier.toString());
	}

	public static boolean isGroundedOrAirborne(LivingEntity living, boolean allowWater) {
		if (living instanceof PlayerEntity player && player.getAbilities().flying) {
			return false;
		}
		if (!allowWater) {
			if (living.isTouchingWater() || living.isSwimming()) {
				return false;
			}
		}
		return !living.isFallFlying() && living.getVehicle() == null && !living.isClimbing();
	}

	public static boolean isGroundedOrAirborne(LivingEntity living) {
		return isGroundedOrAirborne(living, false);
	}

	public static boolean isSubmerged(Entity entity, SubmersionGate gate) {
		for (int i = 0; i < MathHelper.ceil(entity.getHeight()); i++) {
			BlockState blockState = entity.getWorld().getBlockState(entity.getBlockPos().up(i));
			if (gate.allowsWater() && !blockState.isOf(Blocks.BUBBLE_COLUMN) && blockState.getFluidState().isIn(ConventionalFluidTags.WATER)) {
				return true;
			}
			if (gate.allowsLava() && blockState.getFluidState().isIn(ConventionalFluidTags.LAVA)) {
				return true;
			}
			if (gate.allowsPowderSnow() && blockState.isOf(Blocks.POWDER_SNOW)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSufficientlyHigh(Entity entity, double distanceFromGround) {
		return entity.getWorld().raycast(new RaycastContext(entity.getPos(), entity.getPos().add(0, -distanceFromGround, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, entity)).getType() == HitResult.Type.MISS;
	}

	public static boolean isDefaultEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
		ItemEnchantmentsComponent defaultEnchantments = InitializeDefaultEnchantmentsEvent.DEFAULT_ENCHANTMENTS.get(stack.getItem());
		if (defaultEnchantments != null) {
			for (RegistryEntry<Enchantment> foundEnchantment : defaultEnchantments.getEnchantments()) {
				if (foundEnchantment == enchantment) {
					int level = ModConfig.singleLevelMode ? 1 : EnchantmentHelper.getLevel(enchantment, stack);
					if (level == defaultEnchantments.getLevel(enchantment)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean limitCheck(boolean fallback, boolean value) {
		if (ModConfig.enchantmentLimit == 0) {
			return fallback;
		}
		return value;
	}

	public static int getNonDefaultEnchantmentsSize(ItemStack stack, int size) {
		for (RegistryEntry<Enchantment> enchantment : EnchantmentHelper.getEnchantments(stack).getEnchantments()) {
			if (isDefaultEnchantment(stack, enchantment)) {
				size--;
			}
		}
		return size;
	}

	public static boolean shouldBeUnbreakable(ItemStack stack) {
		return ModConfig.disableDurability && !stack.isEmpty() && stack.getMaxDamage() > 0 && !stack.isIn(ModItemTags.RETAINS_DURABILITY);
	}

	public static boolean shouldDisableLoyalty(PersistentProjectileEntity entity) {
		if (ModConfig.enchantedTridentsHaveLoyalty) {
			if (entity.getType().isIn(ModEntityTypeTags.NO_LOYALTY)) {
				return true;
			}
			return !(entity.getOwner() instanceof PlayerEntity);
		}
		return false;
	}

	public static boolean shouldHurt(Entity attacker, Entity hitEntity) {
		if (attacker == null || hitEntity == null) {
			return true;
		}
		if (attacker == hitEntity || attacker.getVehicle() == hitEntity) {
			return false;
		}
		if (hitEntity instanceof PlayerEntity hitPlayer && attacker instanceof PlayerEntity attackingPlayer) {
			return attackingPlayer.shouldDamagePlayer(hitPlayer);
		} else if (hitEntity instanceof Ownable ownable) {
			return shouldHurt(attacker, ownable.getOwner());
		}
		return true;
	}

	public static int getFlooredHealth(LivingEntity living) {
		float percentage = living.getHealth() / living.getMaxHealth();
		return (int) Math.floor(percentage * 10 + 0.5);
	}

	public static int getModifiedMaxLevel(ItemStack stack, int maxLevel) {
		if (EnchancementUtil.hasWeakEnchantments(stack)) {
			return MathHelper.ceil(maxLevel / 2F);
		}
		return maxLevel;
	}

	public static int getOriginalMaxLevel(RegistryEntry<Enchantment> enchantment) {
		return ORIGINAL_MAX_LEVELS.getOrDefault(enchantment.value(), enchantment.value().getMaxLevel());
	}

	public static int alterLevel(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
		return getModifiedMaxLevel(stack, getOriginalMaxLevel(enchantment));
	}

	public static boolean hasAnyEnchantmentsIn(Entity entity, TagKey<Enchantment> tag) {
		if (entity instanceof LivingEntity living) {
			for (ItemStack stack : living.getEquippedItems()) {
				if (EnchantmentHelper.hasAnyEnchantmentsIn(stack, tag)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean hasAnyEnchantmentsWith(Entity entity, ComponentType<?> componentType) {
		if (entity instanceof LivingEntity living) {
			for (ItemStack stack : living.getEquippedItems()) {
				if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, componentType)) {
					return true;
				}
			}
		}
		return false;
	}

	public static float getValue(ComponentType<EnchantmentValueEffect> component, Random random, ItemStack stack, float base) {
		MutableFloat mutableFloat = new MutableFloat(base);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> enchantment.value().modifyValue(component, random, level, mutableFloat));
		return mutableFloat.floatValue();
	}

	public static float getValue(ComponentType<EnchantmentValueEffect> component, LivingEntity entity, float base) {
		MutableFloat mutableFloat = new MutableFloat(base);
		for (ItemStack stack : entity.getArmorItems()) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> enchantment.value().modifyValue(component, entity.getRandom(), level, mutableFloat));
		}
		return mutableFloat.floatValue();
	}

	public static float getListValue(ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> component, LivingEntity entity, float base) {
		MutableFloat mutableFloat = new MutableFloat(base);
		for (ItemStack stack : entity.getArmorItems()) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> enchantment.value().modifyValue(component, (ServerWorld) entity.getWorld(), level, stack, mutableFloat));
		}
		return mutableFloat.floatValue();
	}

	public static float getValue(ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> component, ServerWorld world, ItemStack stack, float base) {
		MutableFloat mutableFloat = new MutableFloat(base);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> enchantment.value().modifyValue(component, world, level, stack, mutableFloat));
		return mutableFloat.floatValue();
	}
}
