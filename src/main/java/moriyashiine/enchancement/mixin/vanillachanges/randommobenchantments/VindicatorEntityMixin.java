/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.randommobenchantments;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

@Mixin(VindicatorEntity.class)
public abstract class VindicatorEntityMixin extends IllagerEntity {
	@Unique
	private static final ItemStack IRON_AXE = new ItemStack(Items.IRON_AXE);

	protected VindicatorEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyArg(method = "addBonusForWave", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;set(Ljava/util/Map;Lnet/minecraft/item/ItemStack;)V"))
	private Map<Enchantment, Integer> enchancement$randomMobEnchantments(Map<Enchantment, Integer> value) {
		if (ModConfig.randomMobEnchantments) {
			return EnchancementUtil.getRandomEnchantment(IRON_AXE, getRandom());
		}
		return value;
	}
}
