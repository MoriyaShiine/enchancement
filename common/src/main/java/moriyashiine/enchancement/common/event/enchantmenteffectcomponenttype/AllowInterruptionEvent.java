package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.tag.EnchancementDamageTypeTags;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class AllowInterruptionEvent implements ServerLivingEntityEvents.AfterDamage {
	public static void init() {
		ServerLivingEntityEvents.AFTER_DAMAGE.register(new AllowInterruptionEvent());
	}

	@Override
	public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
		if (source.getEntity() != null && !source.is(EnchancementDamageTypeTags.DOES_NOT_INTERRUPT)) {
			ItemStack stack = entity.getUseItem();
			if (EnchantmentHelper.has(stack, EnchancementEnchantmentEffectComponentTypes.ALLOW_INTERRUPTION)) {
				entity.releaseUsingItem();
				if (entity instanceof Player player) {
					player.getCooldowns().addCooldown(stack, 20);
				}
				EnchancementEntityComponents.LIGHTNING_DASH.maybeGet(entity).ifPresent(lightningDash -> {
					lightningDash.cancel();
					lightningDash.sync();
				});
			}
		}
	}
}
