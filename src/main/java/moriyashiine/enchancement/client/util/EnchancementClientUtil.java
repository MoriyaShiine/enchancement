package moriyashiine.enchancement.client.util;

import me.shedaniel.math.Color;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class EnchancementClientUtil {
	public static int berserkColor = -1;

	public static int getBerserkColor(LivingEntity living, ItemStack stack) {
		float damageBonus = EnchancementUtil.getBonusBerserkDamage(living, stack);
		if (damageBonus > 0) {
			float other = 1 - damageBonus / EnchancementUtil.getMaxBonusBerserkDamage(stack);
			return Color.ofRGB(1F, other, other).getColor();
		}
		return -1;
	}
}
