package moriyashiine.enchancement.neoforge.common;

import com.google.auto.service.AutoService;
import moriyashiine.enchancement.common.EnchancementService;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.GrindstoneEvent;

@AutoService(EnchancementService.class)
public class EnchancementNeoForgeService implements EnchancementService {
	@Override
	public void initNeoForge() {
		NeoForge.EVENT_BUS.addListener(EnchancementNeoForgeService::grindstoneEvent);
	}

	@Override
	public void initAppleSkinIntegration() {
	}

	private static void grindstoneEvent(GrindstoneEvent.OnTakeItem event) {
		ItemStack topReplacement = EnchancementUtil.replaceTopGrindstoneItem(event.getTopItem(), event.getBottomItem());
		if (topReplacement != null) {
			event.setNewTopItem(topReplacement);
		}
		ItemStack bottomReplacement = EnchancementUtil.replaceBottomGrindstoneItem(event.getBottomItem());
		if (bottomReplacement != null) {
			event.setNewBottomItem(bottomReplacement);
		}
	}
}
