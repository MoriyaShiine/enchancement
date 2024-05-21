/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.perception.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@ModifyReturnValue(method = "hasOutline", at = @At("RETURN"))
	private boolean enchancement$perception(boolean original, Entity entity) {
		if (!original && player != null && entity instanceof LivingEntity living && !living.isSneaking() && !living.isInvisible()) {
			int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.PERCEPTION, player);
			if (level > 0 && entity.distanceTo(player) < level * 8 && !EnchancementUtil.hasEnchantment(ModEnchantments.VEIL, living) && !living.canSee(player)) {
				return true;
			}
		}
		return original;
	}
}
