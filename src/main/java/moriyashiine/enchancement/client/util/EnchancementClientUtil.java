/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.util;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.LeechComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.model.Model;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

public class EnchancementClientUtil {
	public static final Set<KeyBinding> VANILLA_AND_ENCHANCEMENT_KEYBINDINGS = new HashSet<>();

	public static int berserkColor = -1;

	public static boolean allowDuplicateKeybinding(KeyBinding keyBinding) {
		if (keyBinding == null) {
			return false;
		}
		return switch (ModConfig.allowDuplicateKeybindings) {
			case NONE -> false;
			case VANILLA_AND_ENCHANCEMENT -> VANILLA_AND_ENCHANCEMENT_KEYBINDINGS.contains(keyBinding);
			case ALL -> true;
		};
	}

	public static int getBerserkColor(LivingEntity living, ItemStack stack) {
		float damageBonus = EnchancementUtil.getBonusBerserkDamage(living, stack);
		if (damageBonus > 0) {
			float other = 1 - damageBonus / EnchancementUtil.getMaxBonusBerserkDamage(stack);
			return (0xFF << 16) | (((int) (other * 255 + 0.5) & 0xFF) << 8) | ((int) (other * 255 + 0.5) & 0xFF);
		}
		return -1;
	}

	public static void renderLeechTrident(TridentEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Model model, Identifier texture, int light, Runnable runnable, CallbackInfo ci) {
		LeechComponent leechComponent = ModEntityComponents.LEECH.get(entity);
		LivingEntity stuckEntity = leechComponent.getStuckEntity();
		if (stuckEntity != null) {
			float offsetX = MathHelper.sin(leechComponent.getRenderTicks()), offsetZ = MathHelper.cos(leechComponent.getRenderTicks());
			matrices.push();
			matrices.translate(offsetX, 0, offsetZ);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) -MathHelper.wrapDegrees((MathHelper.atan2(stuckEntity.getZ() - entity.getZ() + offsetZ, stuckEntity.getX() - entity.getX() + offsetX) * 57.2957763671875) - 90) + 90));
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(60));
			matrices.translate(0, -leechComponent.getStabTicks(), 0);
			model.render(matrices, ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, model.getLayer(texture), false, entity.isEnchanted()), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
			matrices.pop();
			runnable.run();
			ci.cancel();
		}
	}
}
