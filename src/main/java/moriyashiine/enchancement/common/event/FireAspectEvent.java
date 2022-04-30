package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class FireAspectEvent implements UseBlockCallback {
	@Override
	public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
		if (Enchancement.getConfig().fireAspectWorksAsFlintAndSteel && player.isSneaking() && EnchancementUtil.hasEnchantment(Enchantments.FIRE_ASPECT, player.getStackInHand(hand))) {
			ActionResult result = Items.FLINT_AND_STEEL.useOnBlock(new ItemUsageContext(player, hand, hitResult));
			if (result != ActionResult.FAIL) {
				return result;
			}
		}
		return ActionResult.PASS;
	}
}
