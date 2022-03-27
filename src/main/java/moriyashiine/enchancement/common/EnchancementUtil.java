package moriyashiine.enchancement.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EnchancementUtil {
	public static boolean isGroundedOrJumping(LivingEntity living) {
		if (living instanceof PlayerEntity player && player.getAbilities().flying) {
			return false;
		}
		return !living.isTouchingWater() && !living.isSwimming();
	}
}
