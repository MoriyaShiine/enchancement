/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments.integration.spectrum;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.dafuqs.spectrum.registries.SpectrumResourceConditions;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = SpectrumResourceConditions.class, remap = false)
public class SpectrumResourceConditionsMixin {
	@WrapOperation(method = "enchantmentExistsMatch", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;"))
	private static <T> T enchancement$spectrum$disableDisallowedEnchantments(Registry<T> instance, @Nullable Identifier identifier, Operation<T> original) {
		if (!EnchancementUtil.isEnchantmentAllowed(identifier)) {
			return null;
		}
		return original.call(instance, identifier);
	}
}
