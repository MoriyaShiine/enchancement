package moriyashiine.enchancement.common.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModTags;
import moriyashiine.enchancement.mixin.util.ItemEntityAccessor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.UUID;

public class EnchancementUtil {
	public static final Object2IntMap<PlayerEntity> PACKET_IMMUNITIES = new Object2IntOpenHashMap<>();

	public static final ItemStack BRIMSTONE_STACK;

	static {
		BRIMSTONE_STACK = new ItemStack(Items.LAVA_BUCKET);
		BRIMSTONE_STACK.getOrCreateNbt().putBoolean("Brimstone", true);
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

	public static boolean hasEnchantment(Enchantment enchantment, ItemStack stack) {
		return EnchantmentHelper.getLevel(enchantment, stack) > 0;
	}

	public static boolean hasEnchantment(Enchantment enchantment, LivingEntity living) {
		return EnchantmentHelper.getEquipmentLevel(enchantment, living) > 0;
	}

	public static boolean isGroundedOrJumping(LivingEntity living) {
		if (living instanceof PlayerEntity player && player.getAbilities().flying) {
			return false;
		}
		return !living.isTouchingWater() && !living.isSwimming() && !living.isClimbing();
	}

	public static boolean shouldBeUnbreakable(ItemStack stack) {
		int flag = Enchancement.getConfig().unbreakingChangesFlag;
		if (flag == 0) {
			if (!stack.isEmpty() && stack.getMaxDamage() > 0) {
				return true;
			}
			return !stack.isIn(ModTags.Items.RETAINS_DURABILITY);
		}
		return flag > 0 && EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) >= flag;
	}

	public static boolean shouldHurt(Entity attacker, Entity hitEntity) {
		if (attacker == null || hitEntity == null) {
			return true;
		}
		if (attacker == hitEntity) {
			return false;
		}
		if (hitEntity instanceof PlayerEntity hitPlayer) {
			return attacker instanceof PlayerEntity attackingPlayer && attackingPlayer.shouldDamagePlayer(hitPlayer);
		} else {
			NbtCompound tag = hitEntity.writeNbt(new NbtCompound());
			if (tag.contains("Owner")) {
				UUID owner = tag.getUuid("Owner");
				if (owner.equals(attacker.getUuid())) {
					return false;
				}
				return shouldHurt(attacker, ((ServerWorld) attacker.world).getEntity(owner));
			}
		}
		return true;
	}

	public static float getMaxBonusBerserkDamage(ItemStack stack) {
		float maxBonus = 1;
		for (EntityAttributeModifier modifier : stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
			maxBonus += modifier.getValue();
		}
		return maxBonus;
	}

	public static float getBonusBerserkDamage(LivingEntity living, ItemStack stack) {
		if (living != null && hasEnchantment(ModEnchantments.BERSERK, stack)) {
			float health = living.getMaxHealth();
			float bonus = 0;
			while (health > (int) living.getHealth()) {
				health -= 1;
				bonus += 0.5F;
			}
			return Math.min(bonus, getMaxBonusBerserkDamage(stack));
		}
		return 0;
	}

	public static void tickPacketImmunities() {
		for (PlayerEntity player : PACKET_IMMUNITIES.keySet()) {
			if (PACKET_IMMUNITIES.put(player, PACKET_IMMUNITIES.getInt(player) - 1) < 0) {
				PACKET_IMMUNITIES.removeInt(player);
			}
		}
	}
}
