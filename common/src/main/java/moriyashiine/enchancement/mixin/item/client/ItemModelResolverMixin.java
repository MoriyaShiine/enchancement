package moriyashiine.enchancement.mixin.item.client;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.EnchancementDataComponents;
import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.world.item.effects.AllowLoadingProjectileEffect;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {
	@ModifyVariable(method = "appendItemLayers", at = @At("STORE"), name = "modelId")
	private Identifier enchancement$chargedModel(Identifier modelId, ItemStackRenderState output, ItemStack item, @Nullable @Local(argsOnly = true) ItemOwner owner) {
		Identifier chargedModel = getChargedModel(item, owner);
		if (chargedModel != null) {
			return chargedModel;
		}
		return modelId;
	}

	@Unique
	private static @Nullable Identifier getChargedModel(ItemStack stack, @Nullable ItemOwner owner) {
		if (owner != null && owner.asLivingEntity() instanceof LivingEntity user && user.isUsingItem() && user.getActiveItem() == stack && EnchantmentHelper.has(stack, EnchancementEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE)) {
			Identifier chargedModel = getChargedModel(stack, user.getProjectile(stack).getItem());
			if (chargedModel != null) {
				return chargedModel;
			}
		}
		for (ItemStackTemplate projectile : stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).items()) {
			Identifier chargedModel = getChargedModel(stack, projectile.item().value());
			if (chargedModel != null) {
				return chargedModel;
			}
		}
		return null;
	}

	@Unique
	private static @Nullable Identifier getChargedModel(ItemStack stack, Item item) {
		int brimstoneDamage = stack.getOrDefault(EnchancementDataComponents.BRIMSTONE_DAMAGE, 0);
		if (brimstoneDamage > 0) {
			return Enchancement.id("crossbow_brimstone_" + (brimstoneDamage / 2 - 1));
		}
		if (AllowLoadingProjectileEffect.getItems(stack).contains(item)) {
			return AllowLoadingProjectileEffect.getModel(stack, item);
		}
		return null;
	}
}
