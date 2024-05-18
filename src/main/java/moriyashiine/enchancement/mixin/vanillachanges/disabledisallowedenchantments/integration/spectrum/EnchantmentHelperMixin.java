// todo spectrum
///*
// * All Rights Reserved (c) MoriyaShiine
// */
//
//package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments.integration.spectrum;
//
//import de.dafuqs.spectrum.registries.SpectrumItems;
//import moriyashiine.enchancement.common.util.EnchancementUtil;
//import net.minecraft.enchantment.Enchantment;
//import net.minecraft.enchantment.EnchantmentHelper;
//import net.minecraft.enchantment.Enchantments;
//import net.minecraft.item.ItemStack;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(EnchantmentHelper.class)
//public class EnchantmentHelperMixin {
//	@Inject(method = "getLevel", at = @At("RETURN"), cancellable = true)
//	private static void enchancement$spectrum$replaceDisallowedEnchantments(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
//		if (cir.getReturnValueI() == 0) {
//			if (enchantment == Enchantments.POWER && !EnchancementUtil.isEnchantmentAllowed(enchantment) && stack.isOf(SpectrumItems.BOTTOMLESS_BUNDLE)) {
//				cir.setReturnValue(enchantment.getMaxLevel());
//			} else if (enchantment == Enchantments.QUICK_CHARGE && !EnchancementUtil.isEnchantmentAllowed(enchantment) && stack.isOf(SpectrumItems.KNOWLEDGE_GEM)) {
//				cir.setReturnValue(enchantment.getMaxLevel());
//			} else if (enchantment == Enchantments.SWEEPING && !EnchancementUtil.isEnchantmentAllowed(enchantment) && stack.isOf(SpectrumItems.GLASS_CREST_ULTRA_GREATSWORD)) {
//				cir.setReturnValue(enchantment.getMaxLevel());
//			}
//		}
//	}
//}
