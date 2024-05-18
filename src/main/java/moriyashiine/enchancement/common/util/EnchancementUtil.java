/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.event.InitializeDefaultEnchantmentsEvent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModTags;
import moriyashiine.enchancement.mixin.util.ItemEntityAccessor;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnchancementUtil {
	public static final Object2IntMap<Enchantment> ORIGINAL_MAX_LEVELS = new Object2IntOpenHashMap<>();
	public static final Object2IntMap<PlayerEntity> PACKET_IMMUNITIES = new Object2IntOpenHashMap<>();

	public static final int MAXIMUM_MOVEMENT_MULTIPLIER = 4;

	public static boolean hasScatterShot = false, shouldCancelTargetDamagedEnchantments = false;

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

	@Nullable
	public static Enchantment getRandomEnchantment(ItemStack stack, Random random) {
		List<Enchantment> enchantments = new ArrayList<>();
		for (Enchantment enchantment : Registries.ENCHANTMENT) {
			if (stack.canBeEnchantedWith(enchantment, EnchantingContext.RANDOM_ENCHANTMENT)) {
				enchantments.add(enchantment);
			}
		}
		if (!enchantments.isEmpty()) {
			return enchantments.get(random.nextInt(enchantments.size()));
		}
		return null;
	}

	@Nullable
	public static Enchantment getReplacement(Enchantment enchantment, ItemStack stack) {
		List<Enchantment> enchantments = new ArrayList<>();
		for (Enchantment entry : Registries.ENCHANTMENT) {
			if (entry.isEnabled(FeatureFlags.DEFAULT_ENABLED_FEATURES)) {
				if (stack.isOf(Items.ENCHANTED_BOOK) || stack.canBeEnchantedWith(entry, EnchantingContext.RANDOM_ENCHANTMENT)) {
					enchantments.add(entry);
				}
			}
		}
		if (enchantments.isEmpty()) {
			return null;
		}
		int index = (enchantment.getTranslationKey().hashCode() + stack.getTranslationKey().hashCode()) % enchantments.size();
		if (index < 0) {
			index += enchantments.size();
		}
		return enchantments.get(index);
	}

	public static boolean hasEnchantment(Enchantment enchantment, ItemStack stack) {
		return EnchantmentHelper.getLevel(enchantment, stack) > 0;
	}

	public static boolean hasEnchantment(Enchantment enchantment, Entity entity) {
		return entity instanceof LivingEntity living && EnchantmentHelper.getEquipmentLevel(enchantment, living) > 0;
	}

	public static boolean hasWeakEnchantments(ItemStack stack) {
		if (stack.isIn(ModTags.Items.STRONGLY_ENCHANTED)) {
			return false;
		}
		if (stack.isIn(ModTags.Items.WEAKLY_ENCHANTED)) {
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

	public static boolean isEnchantmentAllowed(Identifier identifier) {
		if (identifier == null) {
			return true;
		}
		if (ModConfig.invertedList) {
			return ModConfig.disallowedEnchantments.contains(identifier.toString());
		}
		return !ModConfig.disallowedEnchantments.contains(identifier.toString());
	}

	public static boolean isEnchantmentAllowed(Enchantment enchantment) {
		return isEnchantmentAllowed(Registries.ENCHANTMENT.getId(enchantment));
	}

	public static boolean ignoreRecipe(RecipeEntry<?> recipe) {
		return false;
		// todo spectrum
//		return Enchancement.isSpectrumLoaded && ModConfig.singleLevelMode && recipe instanceof EnchantmentUpgradeRecipe;
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

	public static boolean isDefaultEnchantment(ItemStack stack, Enchantment enchantment) {
		ItemEnchantmentsComponent defaultEnchantments = InitializeDefaultEnchantmentsEvent.DEFAULT_ENCHANTMENTS.get(stack.getItem());
		if (defaultEnchantments != null) {
			for (RegistryEntry<Enchantment> foundEnchantment : defaultEnchantments.getEnchantments()) {
				if (foundEnchantment.value() == enchantment) {
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
			if (isDefaultEnchantment(stack, enchantment.value())) {
				size--;
			}
		}
		return size;
	}

	public static boolean shouldBeUnbreakable(ItemStack stack) {
		return ModConfig.disableDurability && !stack.isEmpty() && stack.getMaxDamage() > 0 && !stack.isIn(ModTags.Items.RETAINS_DURABILITY);
	}

	public static boolean shouldDisableLoyalty(PersistentProjectileEntity entity) {
		if (ModConfig.tridentsHaveInnateLoyalty) {
			if (entity.getType().isIn(ModTags.EntityTypes.NO_LOYALTY)) {
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
		if (attacker == hitEntity) {
			return false;
		}
		if (hitEntity instanceof PlayerEntity hitPlayer && attacker instanceof PlayerEntity attackingPlayer) {
			return attackingPlayer.shouldDamagePlayer(hitPlayer);
		} else if (hitEntity instanceof Ownable ownable) {
			return shouldHurt(attacker, ownable.getOwner());
		}
		return true;
	}

	public static float capMovementMultiplier(float multiplier) {
		return Math.min(MAXIMUM_MOVEMENT_MULTIPLIER, multiplier);
	}

	public static float getMaxBonusBerserkDamage(ItemStack stack, int level) {
		if (stack.contains(DataComponentTypes.ATTRIBUTE_MODIFIERS)) {
			float divisor = 2F / level;
			if (divisor <= 1E-3) {
				return Integer.MAX_VALUE;
			}
			float maxBonus = 1;
			for (AttributeModifiersComponent.Entry entry : stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS).modifiers()) {
				if (entry.attribute() == EntityAttributes.GENERIC_ATTACK_DAMAGE && entry.slot().matches(EquipmentSlot.MAINHAND)) {
					maxBonus += (float) (entry.modifier().value() / divisor);
				}
			}
			return maxBonus / 2;
		}
		return 0;
	}

	public static float getBonusBerserkDamage(LivingEntity living, ItemStack stack) {
		if (living != null) {
			int level = EnchantmentHelper.getLevel(ModEnchantments.BERSERK, stack);
			if (level > 0) {
				float health = living.getMaxHealth() - 1;
				float bonus = 0;
				while (health > living.getHealth()) {
					health -= 2;
					bonus += level * 0.25F;
				}
				return Math.min(bonus, getMaxBonusBerserkDamage(stack, level));
			}
		}
		return 0;
	}

	public static int getModifiedMaxLevel(ItemStack stack, int maxLevel) {
		if (EnchancementUtil.hasWeakEnchantments(stack)) {
			return MathHelper.ceil(maxLevel / 2F);
		}
		return maxLevel;
	}

	public static int getBrimstoneDamage(float progress) {
		return (int) (6 * progress) * 2;
	}

	public static void tickPacketImmunities() {
		PACKET_IMMUNITIES.object2IntEntrySet().removeIf(entry -> entry.setValue(entry.getIntValue() - 1) <= 0);
	}
}
