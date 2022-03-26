package moriyashiine.enchancement.mixin.client.perception;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@ModifyVariable(method = "update", at = @At("STORE"), ordinal = 3)
	private float enchancement$perceptionNightVision(float value) {
		if (client.player != null && EnchantmentHelper.getLevel(ModEnchantments.PERCEPTION, client.player.getEquippedStack(EquipmentSlot.HEAD)) > 0) {
			return Math.max(1, value);
		}
		return value;
	}
}
