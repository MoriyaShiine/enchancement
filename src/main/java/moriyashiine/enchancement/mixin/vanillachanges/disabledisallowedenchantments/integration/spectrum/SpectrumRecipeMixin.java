// todo spectrum
///*
// * All Rights Reserved (c) MoriyaShiine
// */
//
//package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments.integration.spectrum;
//
//import de.dafuqs.spectrum.items.tools.WorkstaffItem;
//import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
//import de.dafuqs.spectrum.recipe.pedestal.PedestalRecipe;
//import de.dafuqs.spectrum.registries.SpectrumItems;
//import moriyashiine.enchancement.common.init.ModEnchantments;
//import moriyashiine.enchancement.common.util.EnchancementUtil;
//import net.minecraft.enchantment.Enchantment;
//import net.minecraft.enchantment.EnchantmentHelper;
//import net.minecraft.enchantment.Enchantments;
//import net.minecraft.item.ItemStack;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.ModifyVariable;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@Mixin(value = {PedestalRecipe.class, FusionShrineRecipe.class}, remap = false)
//public class SpectrumRecipeMixin {
//	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
//	private static ItemStack enchancement$spectrum$replaceDisallowedEnchantments(ItemStack value) {
//		Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(value);
//		Map<Enchantment, Integer> newMap = new LinkedHashMap<>();
//		for (Enchantment enchantment : enchantments.keySet()) {
//			if (EnchancementUtil.isEnchantmentAllowed(enchantment)) {
//				newMap.put(enchantment, enchantments.get(enchantment));
//			} else {
//				if (value.isOf(SpectrumItems.BEDROCK_HOE)) {
//					newMap.put(Enchantments.EFFICIENCY, Enchantments.EFFICIENCY.getMaxLevel() + 1);
//				} else if (value.isOf(SpectrumItems.LAGOON_ROD)) {
//					newMap.put(Enchantments.LUCK_OF_THE_SEA, Enchantments.LUCK_OF_THE_SEA.getMaxLevel());
//				} else if (value.isOf(SpectrumItems.LUCKY_PICKAXE)) {
//					newMap.put(ModEnchantments.MOLTEN, ModEnchantments.MOLTEN.getMaxLevel());
//				} else if (value.isOf(SpectrumItems.RAZOR_FALCHION)) {
//					newMap.put(ModEnchantments.SCOOPING, ModEnchantments.SCOOPING.getMaxLevel());
//				} else if (value.getItem() instanceof WorkstaffItem) {
//					newMap.put(ModEnchantments.MOLTEN, ModEnchantments.MOLTEN.getMaxLevel());
//				}
//			}
//		}
//		EnchantmentHelper.set(newMap, value);
//		return value;
//	}
//}
