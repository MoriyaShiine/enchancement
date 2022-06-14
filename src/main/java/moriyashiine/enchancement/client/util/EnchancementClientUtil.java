/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.client.util;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
public class EnchancementClientUtil {
	public static int berserkColor = -1;

	public static int getBerserkColor(LivingEntity living, ItemStack stack) {
		float damageBonus = EnchancementUtil.getBonusBerserkDamage(living, stack);
		if (damageBonus > 0) {
			float other = 1 - damageBonus / EnchancementUtil.getMaxBonusBerserkDamage(stack);
			return (0xFF << 16) | (((int) (other * 255 + 0.5) & 0xFF) << 8) | ((int) (other * 255 + 0.5) & 0xFF);
		}
		return -1;
	}

	public static void renderLeechTrident(TridentEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Model model, Identifier texture, int light, Runnable runnable, CallbackInfo ci) {
		ModEntityComponents.LEECH.maybeGet(entity).ifPresent(leechComponent -> {
			LivingEntity stuckEntity = leechComponent.getStuckEntity();
			if (stuckEntity != null) {
				float offsetX = MathHelper.sin(leechComponent.getRenderTicks()), offsetZ = MathHelper.cos(leechComponent.getRenderTicks());
				matrices.push();
				matrices.translate(offsetX, 0, offsetZ);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) -MathHelper.wrapDegrees((MathHelper.atan2(stuckEntity.getZ() - entity.getZ() + offsetZ, stuckEntity.getX() - entity.getX() + offsetX) * 57.2957763671875) - 90) + 90));
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(60));
				matrices.translate(0, -leechComponent.getStabTicks(), 0);
				model.render(matrices, ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, model.getLayer(texture), false, entity.isEnchanted()), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
				matrices.pop();
				runnable.run();
				ci.cancel();
			}
		});
	}
}
