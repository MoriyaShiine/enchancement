// todo spectrum
///*
// * All Rights Reserved (c) MoriyaShiine
// */
//
//package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments.integration.spectrum;
//
//import de.dafuqs.spectrum.items.tools.WorkstaffItem;
//import moriyashiine.enchancement.common.init.ModEnchantments;
//import moriyashiine.enchancement.common.util.EnchancementUtil;
//import net.minecraft.enchantment.Enchantment;
//import net.minecraft.enchantment.Enchantments;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.ModifyArg;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.Map;
//
//@Mixin(WorkstaffItem.class)
//public class WorkstaffItemMixin {
//	@ModifyArg(method = "applyToggle", at = @At(value = "INVOKE", target = "Lde/dafuqs/spectrum/items/tools/WorkstaffItem;enchantAndRemoveOthers(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/text/Text;Lnet/minecraft/enchantment/Enchantment;)V", ordinal = 0))
//	private static Enchantment enchancement$spectrum$replaceDisallowedEnchantments(Enchantment value) {
//		if (!EnchancementUtil.isEnchantmentAllowed(Enchantments.FORTUNE)) {
//			return ModEnchantments.MOLTEN;
//		}
//		return value;
//	}
//
//	@ModifyArg(method = "enchantAndRemoveOthers", at = @At(value = "INVOKE", target = "Lde/dafuqs/spectrum/helpers/SpectrumEnchantmentHelper;removeEnchantments(Lnet/minecraft/item/ItemStack;[Lnet/minecraft/enchantment/Enchantment;)Lnet/minecraft/util/Pair;"))
//	private static Enchantment[] enchancement$spectrum$replaceDisallowedEnchantments(Enchantment[] value) {
//		Enchantment[] values = new Enchantment[value.length + 1];
//		System.arraycopy(value, 0, values, 0, value.length);
//		values[values.length - 1] = ModEnchantments.MOLTEN;
//		return values;
//	}
//
//	@Inject(method = "getDefaultEnchantments", at = @At("HEAD"), cancellable = true, remap = false)
//	private void enchancement$spectrum$replaceDisallowedEnchantments(CallbackInfoReturnable<Map<Enchantment, Integer>> cir) {
//		if (!EnchancementUtil.isEnchantmentAllowed(Enchantments.FORTUNE)) {
//			cir.setReturnValue(Map.of(ModEnchantments.MOLTEN, ModEnchantments.MOLTEN.getMaxLevel()));
//		}
//	}
//}
