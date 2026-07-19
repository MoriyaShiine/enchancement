package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.ModifyStackDamageEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

public class HeadshotEvent implements ModifyStackDamageEvent {
	public static void init() {
		ModifyStackDamageEvent.ADD.register(new HeadshotEvent());
	}

	@Override
	public float modify(ServerLevel level, ItemStack stack, Entity victim, DamageSource source, float damage) {
		if (EnchancementUtil.shouldApplyWeaponEffect() && source.getDirectEntity() instanceof LivingEntity attacker && isHeadshot(attacker, victim)) {
			return EnchancementUtil.getValue(EnchancementEnchantmentEffectComponentTypes.HEADSHOT, level, stack, 0);
		}
		return 0;
	}

	private static boolean isHeadshot(LivingEntity attacker, Entity victim) {
		if (attacker.getAttackRangeWith(attacker.getActiveItem()).getClosesetHit(attacker, 0, entity -> entity == victim) instanceof EntityHitResult result) {
			double distance = (result.getLocation().y() - result.getEntity().getEyeY()) / victim.getBbHeight();
			return distance <= 0.2 && distance > -0.1;
		}
		return false;
	}
}
