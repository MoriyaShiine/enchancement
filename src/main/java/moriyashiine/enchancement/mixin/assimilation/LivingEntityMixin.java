/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.assimilation;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	protected ItemStack activeItemStack;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@WrapOperation(method = "setCurrentHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxUseTime()I"))
	private int enchancement$assimilationHand(ItemStack instance, Operation<Integer> original) {
		return modifyTimeLeft(original.call(instance));
	}

	@WrapOperation(method = "onTrackedDataSet", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxUseTime()I"))
	private int enchancement$assimilationSet(ItemStack instance, Operation<Integer> original) {
		return modifyTimeLeft(original.call(instance));
	}

	@Unique
	private int modifyTimeLeft(int original) {
		if (activeItemStack.contains(DataComponentTypes.FOOD) || activeItemStack.getUseAction() == UseAction.DRINK) {
			int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.ASSIMILATION, (LivingEntity) (Object) this);
			if (level > 0) {
				return Math.max(1, MathHelper.floor(original * (1 - level * 0.125F)));
			}
		}
		return original;
	}
}
