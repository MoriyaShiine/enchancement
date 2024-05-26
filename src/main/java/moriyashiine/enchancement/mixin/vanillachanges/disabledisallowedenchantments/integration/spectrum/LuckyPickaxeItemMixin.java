/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
// todo spectrum
//package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments.integration.spectrum;
//
//import moriyashiine.enchancement.common.init.ModEnchantments;
//import moriyashiine.enchancement.common.util.EnchancementUtil;
//import net.minecraft.enchantment.Enchantment;
//import net.minecraft.enchantment.Enchantments;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.Map;
//
//@Mixin(targets = "de/dafuqs/spectrum/registries/SpectrumItems$2", remap = false)
//public class LuckyPickaxeItemMixin {
//	@Inject(method = "getDefaultEnchantments", at = @At("HEAD"), cancellable = true)
//	private void enchancement$spectrum$replaceDisallowedEnchantments(CallbackInfoReturnable<Map<Enchantment, Integer>> cir) {
//		if (!EnchancementUtil.isEnchantmentAllowed(Enchantments.FORTUNE)) {
//			cir.setReturnValue(Map.of(ModEnchantments.MOLTEN, ModEnchantments.MOLTEN.getMaxLevel()));
//		}
//	}
//}
