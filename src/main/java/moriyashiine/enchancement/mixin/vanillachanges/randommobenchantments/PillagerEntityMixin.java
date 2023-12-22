/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.randommobenchantments;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends IllagerEntity {
	@Unique
	private static final ItemStack CROSSBOW = new ItemStack(Items.CROSSBOW);

	protected PillagerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyArg(method = "addBonusForWave", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;set(Ljava/util/Map;Lnet/minecraft/item/ItemStack;)V"))
	private Map<Enchantment, Integer> enchancement$randomMobEnchantmentsRaid(Map<Enchantment, Integer> value) {
		if (ModConfig.randomMobEnchantments) {
			return EnchancementUtil.getRandomEnchantment(CROSSBOW, getRandom());
		}
		return value;
	}

	@ModifyArg(method = "enchantMainHandItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;set(Ljava/util/Map;Lnet/minecraft/item/ItemStack;)V"))
	private Map<Enchantment, Integer> enchancement$randomMobEnchantments(Map<Enchantment, Integer> value) {
		if (ModConfig.randomMobEnchantments) {
			return EnchancementUtil.getRandomEnchantment(CROSSBOW, getRandom());
		}
		return value;
	}
}
