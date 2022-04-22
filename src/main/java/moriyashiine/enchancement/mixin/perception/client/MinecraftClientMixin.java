package moriyashiine.enchancement.mixin.perception.client;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
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
	private void enchancement$perception(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (player != null && entity instanceof LivingEntity living && !living.isSneaking() && !living.isInvisible() && entity.distanceTo(player) < 16 && EnchancementUtil.hasEnchantment(ModEnchantments.PERCEPTION, player) && !living.canSee(player)) {
			cir.setReturnValue(true);
		}
	}
}
