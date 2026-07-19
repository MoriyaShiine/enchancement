package moriyashiine.enchancement.client.gui.hud;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.EMeterComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class EMeterHudElement implements HudElement {
	private static final Identifier ONE_EMPTY = Enchancement.id("hud/e_meter_1_empty");
	private static final Identifier TWO_EMPTY = Enchancement.id("hud/e_meter_2_empty");
	private static final Identifier THREE_EMPTY = Enchancement.id("hud/e_meter_3_empty");
	private static final Identifier MAX_EMPTY = Enchancement.id("hud/e_meter_max_empty");

	private static final Identifier ONE_FULL = Enchancement.id("hud/e_meter_1_full");
	private static final Identifier TWO_FULL = Enchancement.id("hud/e_meter_2_full");
	private static final Identifier THREE_FULL = Enchancement.id("hud/e_meter_3_full");
	private static final Identifier MAX_FULL = Enchancement.id("hud/e_meter_max_full");
	private static final Identifier MAX_FULL_ALTERNATE = Enchancement.id("hud/e_meter_max_full_alternate");

	public static int yOffset = 0;

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		Minecraft client = Minecraft.getInstance();
		Player player = client.player;
		if (player != null && !player.isSpectator()) {
			EMeterComponent eMeter = EnchancementEntityComponents.E_METER.get(player.getControlledVehicle() instanceof LivingEntity living ? living : player);
			if (eMeter.hasEMeter()) {
				int filledMeters = eMeter.filledMeters();
				int totalWidth = 9 * 8 + 17;
				int x = graphics.guiWidth() / 2 - totalWidth / 2 + 1, y = graphics.guiHeight() - 46 - yOffset;
				for (int i = 0; i < 8; i++) {
					Identifier texture = switch (i / 3) {
						case 2 -> filledMeters > i ? THREE_FULL : THREE_EMPTY;
						case 1 -> filledMeters > i ? TWO_FULL : TWO_EMPTY;
						default -> filledMeters > i ? ONE_FULL : ONE_EMPTY;
					};
					graphics.blitSprite(RenderPipelines.GUI_TEXTURED, texture, x + i * 9, y, 8, 8);
				}
				Identifier texture = filledMeters == 9 ? MAX_FULL : MAX_EMPTY;
				if (eMeter.reachedMax()) {
					texture = client.level.getGameTime() % 6 >= 3 ? MAX_FULL_ALTERNATE : MAX_FULL;
				}
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, texture, x + 8 * 9 - 1, y - 4, 16, 16);
			}
		}
		yOffset = getYOffset(1, 0);
	}

	public static int getYOffset(int numHealthRows, int healthRowHeight) {
		return 12 + (numHealthRows - 1) * healthRowHeight;
	}

	public static boolean shouldMove(Minecraft minecraft) {
		return EMeterHudElement.yOffset < 36 && minecraft.gameMode != null && minecraft.gameMode.canHurtPlayer() && minecraft.player != null && EnchancementEntityComponents.E_METER.get(minecraft.player).hasEMeter();
	}
}
