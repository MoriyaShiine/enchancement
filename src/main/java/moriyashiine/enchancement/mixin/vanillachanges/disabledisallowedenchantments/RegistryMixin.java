package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.enchantment.EmptyEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Registry.class)
public class RegistryMixin {
	@SuppressWarnings("unchecked")
	@ModifyVariable(method = "register(Lnet/minecraft/util/registry/Registry;Lnet/minecraft/util/registry/RegistryKey;Ljava/lang/Object;)Ljava/lang/Object;", at = @At("HEAD"), argsOnly = true)
	private static <V, T extends V> T enchancement$disableDisallowedEnchantments(T value, Registry<V> registry, RegistryKey<V> key) {
		if (registry == Registry.ENCHANTMENT && Enchancement.getConfig().isEnchantmentDisallowed(key.getValue())) {
			return (T) new EmptyEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.ARMOR);
		}
		return value;
	}
}
