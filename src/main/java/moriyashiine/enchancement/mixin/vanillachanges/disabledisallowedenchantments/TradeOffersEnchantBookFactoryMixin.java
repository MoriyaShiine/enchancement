package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Random;

@Mixin(TradeOffers.EnchantBookFactory.class)
public class TradeOffersEnchantBookFactoryMixin {
	@Inject(method = "create", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;get(I)Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void enchancement$disableDisallowedEnchantments(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> cir, List<Enchantment> list) {
		if (!Enchancement.getConfig().allowedEnchantmentIdentifiers.isEmpty()) {
			for (int i = list.size() - 1; i >= 0; i--) {
				if (Enchancement.getConfig().isEnchantmentDisallowed(list.get(i))) {
					list.remove(i);
				}
			}
			if (list.isEmpty()) {
				list.add(Registry.ENCHANTMENT.get(Enchancement.getConfig().allowedEnchantmentIdentifiers.get(random.nextInt(Enchancement.getConfig().allowedEnchantmentIdentifiers.size()))));
			}
		}
	}
}
