package moriyashiine.enchancement.mixin.config.rebalanceenchantments.client;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
	@Unique
	private static final SpriteId ENCHANTED_FIRE_1 = Sheets.BLOCKS_MAPPER.apply(Enchancement.id("enchanted_fire_1"));

	@ModifyArg(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/sprite/SpriteGetter;get(Lnet/minecraft/client/resources/model/sprite/SpriteId;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
	private static SpriteId enchancement$rebalanceEnchantments(SpriteId id) {
		Player player = Minecraft.getInstance().player;
		if (player != null && EnchancementEntityComponents.IGNITED.get(player).isIgnited()) {
			return ENCHANTED_FIRE_1;
		}
		return id;
	}
}
