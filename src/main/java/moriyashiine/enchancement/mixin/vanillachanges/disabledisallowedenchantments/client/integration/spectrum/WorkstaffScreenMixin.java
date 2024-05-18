// todo spectrum
///*
// * All Rights Reserved (c) MoriyaShiine
// */
//
//package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments.client.integration.spectrum;
//
//import de.dafuqs.spectrum.inventories.WorkstaffScreen;
//import moriyashiine.enchancement.common.init.ModEnchantments;
//import moriyashiine.enchancement.common.util.EnchancementUtil;
//import net.minecraft.enchantment.Enchantment;
//import net.minecraft.enchantment.Enchantments;
//import net.minecraft.item.Item;
//import net.minecraft.item.Items;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.ModifyArg;
//
//@Mixin(WorkstaffScreen.class)
//public class WorkstaffScreenMixin {
//	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I"))
//	private Enchantment enchancement$spectrum$replaceDisallowedEnchantments(Enchantment value) {
//		if (!EnchancementUtil.isEnchantmentAllowed(Enchantments.FORTUNE)) {
//			return ModEnchantments.MOLTEN;
//		}
//		return value;
//	}
//
//	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lde/dafuqs/spectrum/inventories/QuickNavigationGridScreen$GridEntry;item(Lnet/minecraft/item/Item;Ljava/lang/String;Lde/dafuqs/spectrum/inventories/QuickNavigationGridScreen$GridEntry$GridEntryCallback;)Lde/dafuqs/spectrum/inventories/QuickNavigationGridScreen$GridEntry;", ordinal = 7))
//	private Item enchancement$spectrum$replaceDisallowedEnchantmentsInit(Item value) {
//		if (!EnchancementUtil.isEnchantmentAllowed(Enchantments.FORTUNE)) {
//			return Items.BLAZE_POWDER;
//		}
//		return value;
//	}
//
//	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lde/dafuqs/spectrum/inventories/QuickNavigationGridScreen$GridEntry;item(Lnet/minecraft/item/Item;Ljava/lang/String;Lde/dafuqs/spectrum/inventories/QuickNavigationGridScreen$GridEntry$GridEntryCallback;)Lde/dafuqs/spectrum/inventories/QuickNavigationGridScreen$GridEntry;", ordinal = 2))
//	private static Item enchancement$spectrum$replaceDisallowedEnchantmentsCLInit(Item value) {
//		if (!EnchancementUtil.isEnchantmentAllowed(Enchantments.FORTUNE)) {
//			return Items.BLAZE_POWDER;
//		}
//		return value;
//	}
//}
