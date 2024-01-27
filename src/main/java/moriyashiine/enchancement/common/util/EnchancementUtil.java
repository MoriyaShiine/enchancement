/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModTags;
import moriyashiine.enchancement.mixin.util.ItemEntityAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchancementUtil {
	public static final Object2IntMap<PlayerEntity> PACKET_IMMUNITIES = new Object2IntOpenHashMap<>();

	public static final ItemStack BRIMSTONE_STACK;

	public static final int MAXIMUM_MOVEMENT_MULTIPLIER = 4;

	public static boolean shouldCancelTargetDamagedEnchantments = false;

	static {
		BRIMSTONE_STACK = new ItemStack(Items.LAVA_BUCKET);
		BRIMSTONE_STACK.getOrCreateSubNbt(Enchancement.MOD_ID).putBoolean("Brimstone", true);
	}

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

	public static Map<Enchantment, Integer> getRandomEnchantment(ItemStack stack, Random random) {
		Map<Enchantment, Integer> map = new HashMap<>();
		List<Enchantment> enchantments = new ArrayList<>();
		for (Enchantment enchantment : Registries.ENCHANTMENT) {
			if (enchantment.isAcceptableItem(stack)) {
				enchantments.add(enchantment);
			}
		}
		if (!enchantments.isEmpty()) {
			Enchantment enchantment = enchantments.get(random.nextInt(enchantments.size()));
			map.put(enchantment, MathHelper.nextInt(random, 1, enchantment.getMaxLevel()));
		}
		return map;
	}

	@Nullable
	public static Enchantment getReplacement(Enchantment enchantment, ItemStack stack) {
		List<Enchantment> enchantments = new ArrayList<>();
		for (Enchantment entry : Registries.ENCHANTMENT) {
			if (stack.isOf(Items.ENCHANTED_BOOK) || entry.isAcceptableItem(stack)) {
				enchantments.add(entry);
			}
		}
		if (enchantments.isEmpty()) {
			return null;
		}
		int index = Registries.ENCHANTMENT.getId(enchantment).hashCode() % enchantments.size();
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
			ArmorMaterial material = armorItem.getMaterial();
			for (ArmorMaterial mat : ArmorMaterials.values()) {
				if (material == mat) {
					return mat == ArmorMaterials.LEATHER || mat == ArmorMaterials.IRON;
				}
			}
			return material.getEnchantability() <= ArmorMaterials.IRON.getEnchantability();
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

	public static boolean isGroundedOrAirborne(LivingEntity living, boolean allowWater) {
		if (living instanceof PlayerEntity player && player.getAbilities().flying) {
			return false;
		}
		if (!allowWater) {
			if (living.isTouchingWater() || living.isSwimming()) {
				return false;
			}
		}
		return !living.isClimbing() && living.getVehicle() == null;
	}

	public static boolean isGroundedOrAirborne(LivingEntity living) {
		return isGroundedOrAirborne(living, false);
	}

	public static boolean isSubmerged(Entity entity, boolean allowWater, boolean allowLava, boolean allowPowderSnow) {
		for (int i = 0; i <= 1; i++) {
			FluidState fluidState = entity.getWorld().getFluidState(entity.getBlockPos().up(i));
			if ((allowWater && fluidState.isIn(FluidTags.WATER)) || (allowLava && fluidState.isIn(FluidTags.LAVA)) || (allowPowderSnow && entity.getWorld().getBlockState(entity.getBlockPos().up(i)).isOf(Blocks.POWDER_SNOW))) {
				return true;
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

	public static boolean shouldBeUnbreakable(ItemStack stack) {
		int flag = ModConfig.unbreakingChangesFlag;
		if (flag >= 0 && !stack.isIn(ModTags.Items.RETAINS_DURABILITY)) {
			if (flag == 0) {
				return !stack.isEmpty() && stack.getMaxDamage() > 0;
			}
			return EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) >= flag;
		}
		return false;
	}

	public static boolean shouldDisableLoyalty(PersistentProjectileEntity entity) {
		if (ModConfig.allTridentsHaveLoyalty) {
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
		float maxBonus = 1;
		for (EntityAttributeModifier modifier : stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
			maxBonus += (float) (modifier.getValue() / (level == 1 ? 2 : 1));
		}
		return maxBonus / 2;
	}

	public static float getBonusBerserkDamage(LivingEntity living, ItemStack stack) {
		if (living != null) {
			int level = EnchantmentHelper.getLevel(ModEnchantments.BERSERK, stack);
			if (level > 0) {
				float health = living.getMaxHealth() - 1;
				float bonus = 0;
				while (health > living.getHealth()) {
					health -= 2;
					bonus += level == 1 ? 0.25F : 0.5F;
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
