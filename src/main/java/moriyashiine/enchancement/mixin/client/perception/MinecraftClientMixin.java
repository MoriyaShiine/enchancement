package moriyashiine.enchancement.mixin.client.perception;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
	private void enchancement$perceptionXray(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (player != null && entity instanceof LivingEntity living && !living.isSneaking() && !living.isInvisible() && entity.distanceTo(player) < 16 && EnchantmentHelper.getEquipmentLevel(ModEnchantments.PERCEPTION, player) > 0 && !living.canSee(player)) {
			cir.setReturnValue(true);
		}
	}
}
