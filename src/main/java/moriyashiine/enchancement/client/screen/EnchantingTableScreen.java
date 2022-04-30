package moriyashiine.enchancement.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.tooltipfix.Helper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class EnchantingTableScreen extends HandledScreen<EnchantingTableScreenHandler> {
	private static final Identifier TEXTURE = new Identifier(Enchancement.MOD_ID, "textures/gui/container/enchanting_table.png");

	private List<Text> infoTexts = null;

	public EnchantingTableScreen(EnchantingTableScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		int posX = (width - backgroundWidth) / 2;
		int posY = (height - backgroundHeight) / 2;
		RenderSystem.setShaderTexture(0, TEXTURE);
		drawTexture(matrices, posX, posY, 0, 0, backgroundWidth, backgroundHeight);
		if (client != null && client.player != null && handler.canEnchant(client.player, client.player.isCreative())) {
			if (isInEnchantButtonBounds(posX, posY, mouseX, mouseY)) {
				drawTexture(matrices, posX + 78, posY + 14, 0, 204, 90, 19);
				drawTexture(matrices, posX + 59, posY + 14, 90, 204, 19, 19);
			} else {
				drawTexture(matrices, posX + 78, posY + 14, 0, 166, 90, 19);
				drawTexture(matrices, posX + 59, posY + 14, 90, 166, 19, 19);
			}
			drawTexture(matrices, posX + 61, posY + 15, 0, 223, 16, 16);
			textRenderer.draw(matrices, handler.enchantmentName, posX + 82, posY + 19, 0xFFFFFF);
		} else {
			drawTexture(matrices, posX + 78, posY + 14, 0, 185, 90, 19);
			if (!handler.enchantmentName.isEmpty()) {
				drawTexture(matrices, posX + 61, posY + 15, 0, 239, 16, 16);
				textRenderer.draw(matrices, handler.enchantmentName, posX + 82, posY + 19, 0x7F7F7F);
			}
		}
		if (handler.slots.get(0).hasStack() && handler.slots.get(0).getStack().isEnchantable()) {
			RenderSystem.setShaderTexture(0, TEXTURE);
			if (isInLeftButtonBounds(posX, posY, mouseX, mouseY)) {
				drawTexture(matrices, posX + 68, posY + 45, 90, 204, 19, 19);
			} else {
				drawTexture(matrices, posX + 68, posY + 45, 90, 166, 19, 19);
			}
			if (isInRightButtonBounds(posX, posY, mouseX, mouseY)) {
				drawTexture(matrices, posX + 140, posY + 45, 90, 204, 19, 19);
			} else {
				drawTexture(matrices, posX + 140, posY + 45, 90, 166, 19, 19);
			}
			textRenderer.draw(matrices, "<", posX + 75, posY + 51, 0xFFFFFF);
			textRenderer.draw(matrices, ">", posX + 148, posY + 51, 0xFFFFFF);
		}
		if (client.currentScreen != null && handler.enchantmentDescription != null && mouseX >= posX + 78 && mouseX <= posX + 168 && mouseY >= posY + 14 && mouseY <= posY + 33) {
			if (infoTexts == null) {
				infoTexts = Helper.doFix(List.of(new LiteralText(handler.enchantmentName).formatted(Formatting.GRAY), handler.enchantmentDescription.formatted(Formatting.DARK_GRAY)), textRenderer);
			}
			client.currentScreen.renderTooltip(matrices, infoTexts, mouseX, mouseY);
		} else {
			infoTexts = null;
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int posX = (width - backgroundWidth) / 2;
		int posY = (height - backgroundHeight) / 2;
		if (isInEnchantButtonBounds(posX, posY, (int) mouseX, (int) mouseY) && handler.onButtonClick(client.player, 0)) {
			client.interactionManager.clickButton(handler.syncId, 0);
			return true;
		} else if (handler.canEnchant(client.player, true) && isInLeftButtonBounds(posX, posY, (int) mouseX, (int) mouseY) && handler.onButtonClick(client.player, 1)) {
			client.interactionManager.clickButton(handler.syncId, 1);
			client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1));
			return true;
		} else if (handler.canEnchant(client.player, true) && isInRightButtonBounds(posX, posY, (int) mouseX, (int) mouseY) && handler.onButtonClick(client.player, 2)) {
			client.interactionManager.clickButton(handler.syncId, 2);
			client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1));
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	private static boolean isInEnchantButtonBounds(int posX, int posY, int mouseX, int mouseY) {
		return mouseX >= posX + 59 && mouseX <= posX + 168 && mouseY >= posY + 14 && mouseY <= posY + 33;
	}

	private static boolean isInLeftButtonBounds(int posX, int posY, int mouseX, int mouseY) {
		return mouseX >= posX + 68 && mouseX <= posX + 87 && mouseY >= posY + 45 && mouseY <= posY + 64;
	}

	private static boolean isInRightButtonBounds(int posX, int posY, int mouseX, int mouseY) {
		return mouseX >= posX + 140 && mouseX <= posX + 159 && mouseY >= posY + 45 && mouseY <= posY + 64;
	}
}
