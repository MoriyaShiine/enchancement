/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.util;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.LightningDashComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.tag.ModItemTags;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryOwner;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnchancementUtil {
	public static RegistryEntryOwner<?> ENCHANTMENT_REGISTRY_OWNER = null;
	public static final List<RegistryEntry.Reference<Enchantment>> ENCHANTMENTS = new ArrayList<>();
	public static Random SERVER_RANDOM = null;

	public static final Map<Enchantment, Integer> ORIGINAL_MAX_LEVELS = new ConcurrentHashMap<>();
	public static final Map<TagKey<Item>, TriState> VANILLA_ENCHANTMENT_STRENGTH_TAGS = new HashMap<>();

	static {
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_LEATHER_ARMOR, TriState.TRUE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_CHAIN_ARMOR, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_IRON_ARMOR, TriState.TRUE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_GOLD_ARMOR, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_DIAMOND_ARMOR, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_NETHERITE_ARMOR, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_TURTLE_HELMET, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_WOLF_ARMOR, TriState.FALSE);

		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.WOODEN_TOOL_MATERIALS, TriState.TRUE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.STONE_TOOL_MATERIALS, TriState.TRUE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.IRON_TOOL_MATERIALS, TriState.TRUE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.GOLD_TOOL_MATERIALS, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.DIAMOND_TOOL_MATERIALS, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.NETHERITE_TOOL_MATERIALS, TriState.FALSE);
	}

	public static ItemStack cachedApplyStack = null;

	public static boolean shouldCancelTargetDamagedEnchantments = false;

	public static List<ItemEntity> mergeItemEntities(List<ItemEntity> drops) {
		for (int i = drops.size() - 1; i >= 0; i--) {
			if (i < drops.size() - 1) {
				ItemEntity itemEntity = drops.get(i);
				ItemEntity other = drops.get(i + 1);
				itemEntity.tryMerge(other);
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
		return enchantment.value().description().getString();
	}

	// disable disallowed enchantments

	@Nullable
	public static RegistryEntry<Enchantment> getRandomEnchantment(ItemStack stack, TagKey<Enchantment> checkedTag, @Nullable Random random) {
		List<RegistryEntry<Enchantment>> enchantments = new ArrayList<>();
		for (RegistryEntry<Enchantment> enchantment : ENCHANTMENTS) {
			if (enchantment.isIn(checkedTag)) {
				if (stack.isOf(Items.BOOK) || stack.isOf(Items.ENCHANTED_BOOK) || stack.canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE)) {
					enchantments.add(enchantment);
				}
			}
		}
		if (!enchantments.isEmpty()) {
			if (random == null) {
				random = SERVER_RANDOM;
			}
			return enchantments.get(random.nextInt(enchantments.size()));
		}
		return null;
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

	// single level mode

	public static boolean hasWeakEnchantments(ItemStack stack) {
		if (stack.isIn(ModItemTags.STRONGLY_ENCHANTED)) {
			return false;
		}
		if (stack.isIn(ModItemTags.WEAKLY_ENCHANTED)) {
			return true;
		}
		int enchantmentValue = getEnchantmentValue(stack);
		if (enchantmentValue > 0) {
			TagKey<Item> repairTag = stack.contains(DataComponentTypes.REPAIRABLE) ? stack.get(DataComponentTypes.REPAIRABLE).items().getTagKey().orElse(null) : null;
			if (repairTag != null) {
				TriState triState = VANILLA_ENCHANTMENT_STRENGTH_TAGS.getOrDefault(repairTag, TriState.DEFAULT);
				if (triState != TriState.DEFAULT) {
					return triState.asBoolean(false);
				}
			}
			return enchantmentValue <= (stack.isIn(ItemTags.ARMOR_ENCHANTABLE) ? ArmorMaterials.IRON.enchantmentValue() : ToolMaterial.IRON.enchantmentValue());
		}
		return false;
	}

	public static int alterLevel(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
		if (ModConfig.singleLevelMode && enchantment.matchesKey(Enchantments.WIND_BURST)) {
			return 1;
		}
		return getModifiedMaxLevel(stack, getOriginalMaxLevel(enchantment));
	}

	public static int getEnchantmentValue(ItemStack stack) {
		if (stack.contains(DataComponentTypes.ENCHANTABLE)) {
			int value = stack.get(DataComponentTypes.ENCHANTABLE).value();
			if (value == 1) {
				value = (stack.isIn(ItemTags.ARMOR_ENCHANTABLE) ? ArmorMaterials.IRON.enchantmentValue() : ToolMaterial.IRON.enchantmentValue()) + 1;
			}
			return value;
		}
		return 0;
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

	// enchantment limit

	public static boolean isDefaultEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
		ItemEnchantmentsComponent defaultEnchantments = stack.getItem().getComponents().getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
		for (RegistryEntry<Enchantment> foundEnchantment : defaultEnchantments.getEnchantments()) {
			if (foundEnchantment == enchantment) {
				int level = ModConfig.singleLevelMode ? 1 : EnchantmentHelper.getLevel(enchantment, stack);
				if (level == defaultEnchantments.getLevel(enchantment)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean exceedsLimit(ItemStack stack, int size) {
		if (ModConfig.enchantmentLimit == 0) {
			return false;
		}
		for (RegistryEntry<Enchantment> enchantment : EnchantmentHelper.getEnchantments(stack).getEnchantments()) {
			if (isDefaultEnchantment(stack, enchantment)) {
				size--;
			}
		}
		return size > ModConfig.enchantmentLimit;
	}

	// disable durability

	public static boolean isUnbreakable(ItemStack stack) {
		return ModConfig.disableDurability && !stack.isEmpty() && stack.contains(DataComponentTypes.MAX_DAMAGE) && !stack.isIn(ModItemTags.RETAINS_DURABILITY);
	}

	// misc

	public static List<ItemStack> getArmorItems(LivingEntity entity) {
		List<ItemStack> stacks = new ArrayList<>();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.isArmorSlot()) {
				stacks.add(entity.getEquippedStack(slot));
			}
		}
		return stacks;
	}

	public static List<ItemStack> getHeldItems(LivingEntity entity) {
		List<ItemStack> stacks = new ArrayList<>();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.HAND) {
				stacks.add(entity.getEquippedStack(slot));
			}
		}
		return stacks;
	}

	public static boolean isBodyArmor(ItemStack stack) {
		return stack.contains(DataComponentTypes.EQUIPPABLE) && stack.get(DataComponentTypes.EQUIPPABLE).slot() == EquipmentSlot.BODY;
	}

	public static int getFlooredHealth(LivingEntity living) {
		float percentage = living.getHealth() / living.getMaxHealth();
		return (int) Math.floor(percentage * 10 + 0.5);
	}

	public static void resetFallDistance(Entity entity) {
		entity.onLanding();
		ModEntityComponents.LIGHTNING_DASH.maybeGet(entity).ifPresent(LightningDashComponent::cancel);
	}

	// enchantment

	public static boolean hasAnyEnchantmentsIn(Entity entity, TagKey<Enchantment> tag) {
		if (entity instanceof LivingEntity living) {
			for (ItemStack stack : getArmorItems(living)) {
				if (EnchantmentHelper.hasAnyEnchantmentsIn(stack, tag)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean hasAnyEnchantmentsWith(Entity entity, ComponentType<?> componentType) {
		if (entity instanceof LivingEntity living) {
			for (ItemStack stack : getArmorItems(living)) {
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
		for (ItemStack stack : getArmorItems(entity)) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> enchantment.value().modifyValue(component, entity.getRandom(), level, mutableFloat));
		}
		return mutableFloat.floatValue();
	}

	public static float getValue(ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> component, ServerWorld world, ItemStack stack, float base) {
		MutableFloat mutableFloat = new MutableFloat(base);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> enchantment.value().modifyValue(component, world, level, stack, mutableFloat));
		return mutableFloat.floatValue();
	}

	// specific enchantment

	public static boolean shouldFluidWalk(Entity entity) {
		return !SLibUtils.isCrouching(entity, true) && hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.FLUID_WALKING);
	}
}
