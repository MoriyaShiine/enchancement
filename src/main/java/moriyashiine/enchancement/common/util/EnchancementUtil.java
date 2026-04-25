/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.LightningDashComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.tag.ModItemTags;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnchancementUtil {
	public static HolderOwner<?> ENCHANTMENT_HOLDER_OWNER = null;
	public static final List<Holder.Reference<Enchantment>> ENCHANTMENTS = new ArrayList<>();
	public static RandomSource SERVER_RANDOM = null;

	public static final Map<ResourceKey<Enchantment>, Integer> ORIGINAL_MAX_LEVELS = new ConcurrentHashMap<>();
	public static final Map<TagKey<Item>, TriState> VANILLA_ENCHANTMENT_STRENGTH_TAGS = new Object2ObjectArrayMap<>();

	static {
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_LEATHER_ARMOR, TriState.TRUE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_COPPER_ARMOR, TriState.TRUE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_CHAIN_ARMOR, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_IRON_ARMOR, TriState.TRUE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_GOLD_ARMOR, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_DIAMOND_ARMOR, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_NETHERITE_ARMOR, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_TURTLE_HELMET, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.REPAIRS_WOLF_ARMOR, TriState.FALSE);

		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.WOODEN_TOOL_MATERIALS, TriState.TRUE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.STONE_TOOL_MATERIALS, TriState.TRUE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.COPPER_TOOL_MATERIALS, TriState.TRUE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.IRON_TOOL_MATERIALS, TriState.TRUE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.GOLD_TOOL_MATERIALS, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.DIAMOND_TOOL_MATERIALS, TriState.FALSE);
		VANILLA_ENCHANTMENT_STRENGTH_TAGS.put(ItemTags.NETHERITE_TOOL_MATERIALS, TriState.FALSE);
	}

	public static ItemStack cachedApplyStack = null;

	public static List<ItemEntity> mergeItemEntities(List<ItemEntity> drops) {
		for (int i = drops.size() - 1; i >= 0; i--) {
			if (i < drops.size() - 1) {
				ItemEntity itemEntity = drops.get(i);
				ItemEntity other = drops.get(i + 1);
				itemEntity.tryToMerge(other);
				if (itemEntity.getItem().isEmpty()) {
					drops.remove(i);
				}
				if (other.getItem().isEmpty()) {
					drops.remove(i + 1);
				}
			}
		}
		return drops;
	}

	public static String getTranslationKey(Holder<Enchantment> enchantment) {
		if (enchantment.value().description().getContents() instanceof TranslatableContents translatable) {
			return translatable.getKey();
		}
		return enchantment.value().description().getString();
	}

	// disable disallowed enchantments

	public static @Nullable Holder<Enchantment> getRandomEnchantment(ItemStack stack, TagKey<Enchantment> checkedTag, @Nullable RandomSource random) {
		List<Holder<Enchantment>> enchantments = new ArrayList<>();
		for (Holder<Enchantment> enchantment : ENCHANTMENTS) {
			if (enchantment.is(checkedTag)) {
				if (stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK) || stack.canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE)) {
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

	public static boolean isEnchantmentAllowed(Holder<Enchantment> enchantment) {
		if (enchantment.unwrapKey().isPresent()) {
			return isEnchantmentAllowed(enchantment.unwrapKey().get().identifier());
		}
		return false;
	}

	public static boolean isEnchantmentAllowed(Identifier identifier) {
		if (identifier.equals(ModEnchantments.EMPTY_KEY.identifier())) {
			return false;
		}
		if (ModConfig.invertedList) {
			return ModConfig.disallowedEnchantments.contains(identifier.toString());
		}
		return !ModConfig.disallowedEnchantments.contains(identifier.toString());
	}

	// single level mode

	public static boolean hasWeakEnchantments(ItemInstance item) {
		if (item.is(ModItemTags.STRONGLY_ENCHANTED)) {
			return false;
		}
		if (item.is(ModItemTags.WEAKLY_ENCHANTED)) {
			return true;
		}
		int enchantmentValue = getEnchantmentValue(item);
		if (enchantmentValue > 0) {
			Repairable repairable = item.get(DataComponents.REPAIRABLE);
			TagKey<Item> repairTag = repairable != null ? repairable.items().unwrapKey().orElse(null) : null;
			if (repairTag != null) {
				TriState triState = VANILLA_ENCHANTMENT_STRENGTH_TAGS.getOrDefault(repairTag, TriState.DEFAULT);
				if (triState != TriState.DEFAULT) {
					return triState.toBoolean(false);
				}
			}
			return enchantmentValue <= (item.is(ItemTags.ARMOR_ENCHANTABLE) ? ArmorMaterials.IRON.enchantmentValue() : ToolMaterial.IRON.enchantmentValue());
		}
		return false;
	}

	public static int alterLevel(ItemInstance item, Holder<Enchantment> enchantment) {
		if (ModConfig.singleLevelMode && enchantment.is(Enchantments.WIND_BURST)) {
			return 1;
		}
		return getModifiedMaxLevel(item, getOriginalMaxLevel(enchantment));
	}

	public static int getEnchantmentValue(ItemInstance item) {
		Enchantable enchantable = item.get(DataComponents.ENCHANTABLE);
		if (enchantable != null) {
			int value = enchantable.value();
			if (value == 1) {
				value = (item.is(ItemTags.ARMOR_ENCHANTABLE) ? ArmorMaterials.IRON.enchantmentValue() : ToolMaterial.IRON.enchantmentValue()) + 1;
			}
			return value;
		}
		return 0;
	}

	public static int getModifiedMaxLevel(ItemInstance item, int maxLevel) {
		if (hasWeakEnchantments(item)) {
			return Mth.ceil(maxLevel / 2F);
		}
		return maxLevel;
	}

	public static int getOriginalMaxLevel(Holder<Enchantment> enchantment) {
		return ORIGINAL_MAX_LEVELS.getOrDefault(enchantment.unwrapKey().orElseThrow(), enchantment.value().getMaxLevel());
	}

	// enchantment limit

	public static boolean isDefaultEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
		ItemEnchantments defaultEnchantments = stack.getItem().components().getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
		for (Holder<Enchantment> foundEnchantment : defaultEnchantments.keySet()) {
			if (foundEnchantment == enchantment) {
				int level = ModConfig.singleLevelMode ? 1 : EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
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
		for (Holder<Enchantment> enchantment : EnchantmentHelper.getEnchantmentsForCrafting(stack).keySet()) {
			if (isDefaultEnchantment(stack, enchantment)) {
				size--;
			}
		}
		return size > ModConfig.enchantmentLimit;
	}

	// disable durability

	public static boolean isUnbreakable(ItemStack stack) {
		return ModConfig.disableDurability && !stack.isEmpty() && stack.has(DataComponents.MAX_DAMAGE) && !stack.is(ModItemTags.RETAINS_DURABILITY);
	}

	// rebalance equipment

	public static float getItemUseSpeedMultiplier(ItemStack stack, float original) {
		if (isFastItem(stack)) {
			return Math.min(1, original * 3);
		}
		return original;
	}

	public static boolean isFastItem(ItemStack stack) {
		if (ModConfig.rebalanceEquipment) {
			Item item = stack.getItem();
			return item instanceof BowItem || item instanceof MaceItem || item instanceof TridentItem;
		}
		return false;
	}

	public static boolean insertToCorrectTridentSlot(AbstractArrow arrow, Inventory inventory, ItemStack stack) {
		if (ModConfig.rebalanceEquipment && arrow instanceof ThrownTrident) {
			int slot = ModEntityComponents.OWNED_TRIDENT.get(arrow).getSlot();
			if (slot >= 0 && slot < inventory.getContainerSize() && inventory.getItem(slot).isEmpty()) {
				inventory.setItem(slot, stack);
				return true;
			}
		}
		return false;
	}

	public static int getTridentChargeTime() {
		return TridentItem.THROW_THRESHOLD_TIME * (ModConfig.rebalanceEquipment ? 2 : 1);
	}

	// misc

	public static List<ItemStack> getArmorItems(LivingEntity entity) {
		List<ItemStack> stacks = new ArrayList<>();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.isArmor()) {
				stacks.add(entity.getItemBySlot(slot));
			}
		}
		return stacks;
	}

	public static List<ItemStack> getHeldItems(LivingEntity entity) {
		List<ItemStack> stacks = new ArrayList<>();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.HAND) {
				stacks.add(entity.getItemBySlot(slot));
			}
		}
		return stacks;
	}

	public static boolean isGroundAnimalArmor(ItemStack stack) {
		return stack.is(ConventionalItemTags.HORSE_ARMORS) || stack.is(ConventionalItemTags.WOLF_ARMORS);
	}

	public static double altLog(double base, double value, double multiplier) {
		return Math.log(value + 1) / Math.log(base) * multiplier;
	}

	public static double logistic(double asymptote, double value) {
		return asymptote * 2 / (1 + Math.pow(Math.E, -value / (asymptote / 2))) - asymptote;
	}

	public static int getFlooredHealth(LivingEntity living) {
		float percentage = living.getHealth() / living.getMaxHealth();
		return (int) Math.floor(percentage * 10 + 0.5);
	}

	public static void resetFallDistance(Entity entity) {
		entity.resetFallDistance();
		ModEntityComponents.LIGHTNING_DASH.maybeGet(entity).ifPresent(LightningDashComponent::cancel);
	}

	// enchantment

	public static boolean hasAnyEnchantmentsIn(Entity entity, TagKey<Enchantment> tagKey) {
		if (entity instanceof LivingEntity living) {
			for (ItemStack stack : getArmorItems(living)) {
				if (EnchantmentHelper.hasTag(stack, tagKey)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean hasAnyEnchantmentsWith(Entity entity, DataComponentType<?> componentType) {
		if (entity instanceof LivingEntity living) {
			for (ItemStack stack : getArmorItems(living)) {
				if (EnchantmentHelper.has(stack, componentType)) {
					return true;
				}
			}
		}
		return false;
	}

	public static float getValue(DataComponentType<EnchantmentValueEffect> component, RandomSource random, ItemStack stack, float base) {
		MutableFloat mutableFloat = new MutableFloat(base);
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> enchantment.value().modifyUnfilteredValue(component, random, level, mutableFloat));
		return mutableFloat.floatValue();
	}

	public static float getValue(DataComponentType<EnchantmentValueEffect> component, LivingEntity entity, float base) {
		MutableFloat mutableFloat = new MutableFloat(base);
		for (ItemStack stack : getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> enchantment.value().modifyUnfilteredValue(component, entity.getRandom(), level, mutableFloat));
		}
		return mutableFloat.floatValue();
	}

	public static float getValue(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> component, ServerLevel world, ItemStack stack, float base) {
		MutableFloat mutableFloat = new MutableFloat(base);
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> enchantment.value().modifyItemFilteredCount(component, world, level, stack, mutableFloat));
		return mutableFloat.floatValue();
	}

	// specific

	public static final VoxelShape FLUID_WALKING_SHAPE = Block.column(16, 0, 8);

	public static boolean shouldFluidWalk(Entity entity) {
		return !SLibUtils.isCrouching(entity, true) && hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.FLUID_WALKING);
	}
}
