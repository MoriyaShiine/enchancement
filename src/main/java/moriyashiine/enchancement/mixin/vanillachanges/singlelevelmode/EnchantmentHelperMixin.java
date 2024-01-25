/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.singlelevelmode;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyVariable(method = "createNbt", at = @At(value = "HEAD"), argsOnly = true)
	private static int enchancement$singleLevelModeNbt(int value) {
		if (ModConfig.singleLevelMode) {
			return 1;
		}
		return value;
	}

	@Inject(method = "forEachEnchantment(Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
	private static void enchancement$singleLevelMode(EnchantmentHelper.Consumer consumer, ItemStack stack, CallbackInfo ci) {
		if (ModConfig.singleLevelMode) {
			if (stack.isEmpty()) {
				return;
			}
			NbtList nbtList = stack.getEnchantments();
			for (int i = 0; i < nbtList.size(); i++) {
				NbtCompound nbtCompound = nbtList.getCompound(i);
				Registries.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound)).ifPresent(enchantment -> consumer.accept(enchantment, getModifiedMaxLevel(stack, enchantment.getMaxLevel())));
			}
			ci.cancel();
		}
	}

	@Inject(method = "getLevel", at = @At("RETURN"), cancellable = true)
	private static void enchancement$singleLevelMode(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (cir.getReturnValueI() > 0 && ModConfig.singleLevelMode) {
			cir.setReturnValue(getModifiedMaxLevel(stack, enchantment.getMaxLevel()));
		}
	}

	@ModifyExpressionValue(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"))
	private static int enchancement$singleLevelModeEntries(int value) {
		if (ModConfig.singleLevelMode) {
			return 1;
		}
		return value;
	}

	@Unique
	private static int getModifiedMaxLevel(ItemStack stack, int maxLevel) {
		boolean weak = false;
		if (stack.getItem() instanceof ArmorItem armorItem) {
			ArmorMaterial material = armorItem.getMaterial();
			weak = material == ArmorMaterials.LEATHER || material == ArmorMaterials.IRON || material == ArmorMaterials.CHAIN;
			if (!weak && Arrays.stream(ArmorMaterials.values()).noneMatch(armorMaterial -> armorMaterial == material)) {
				weak = material.getEnchantability() <= ArmorMaterials.IRON.getEnchantability();
			}
		} else if (stack.getItem() instanceof ToolItem toolItem) {
			ToolMaterial material = toolItem.getMaterial();
			weak = material == ToolMaterials.WOOD || material == ToolMaterials.STONE || material == ToolMaterials.IRON;
			if (!weak && Arrays.stream(ToolMaterials.values()).noneMatch(toolMaterial -> toolMaterial == material)) {
				weak = material.getEnchantability() <= ToolMaterials.IRON.getEnchantability();
			}
		}
		if (weak) {
			return MathHelper.ceil(maxLevel / 2F);
		}
		return maxLevel;
	}
}
