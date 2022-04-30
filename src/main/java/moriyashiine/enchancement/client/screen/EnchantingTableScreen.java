package moriyashiine.enchancement.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.tooltipfix.Helper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class EnchantingTableScreen extends HandledScreen<EnchantingTableScreenHandler> {
	private static final Identifier TEXTURE = new Identifier(Enchancement.MOD_ID, "textures/gui/container/enchanting_table.png");
	private static final Identifier BOOK_TEXTURE = new Identifier("textures/entity/enchanting_table_book.png");

	private List<Text> infoTexts = null;
	private BookModel bookModel;
	private ItemStack stack = ItemStack.EMPTY;
	private float pageAngle;
	private float nextPageAngle;
	private float approximatePageAngle;
	private float pageTurningSpeed;
	private float nextPageTurningSpeed;
	private float pageRotationSpeed;

	public EnchantingTableScreen(EnchantingTableScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		bookModel = new BookModel(client.getEntityModelLoader().getModelPart(EntityModelLayers.BOOK));
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
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
		renderBook(matrices, client.getTickDelta());
	}

	@Override
	protected void handledScreenTick() {
		ItemStack stack = handler.getSlot(0).getStack();
		if (!ItemStack.areEqual(stack, this.stack)) {
			this.stack = stack;
			while (nextPageAngle <= approximatePageAngle + 1 && nextPageAngle >= approximatePageAngle - 1) {
				approximatePageAngle += (client.player.getRandom().nextInt(4) - client.player.getRandom().nextInt(4));
			}
		}
		pageAngle = nextPageAngle;
		pageTurningSpeed = nextPageTurningSpeed;
		nextPageTurningSpeed = handler.slots.get(0).getStack().isEnchantable() ? nextPageTurningSpeed + 0.2F : nextPageTurningSpeed - 0.2F;
		nextPageTurningSpeed = MathHelper.clamp(nextPageTurningSpeed, 0, 1);
		pageRotationSpeed += (MathHelper.clamp((approximatePageAngle - nextPageAngle) * 0.4F, -0.2F, 0.2F) - pageRotationSpeed) * 0.9F;
		nextPageAngle += pageRotationSpeed;
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
			nextPageAngle -= 1;
			return true;
		} else if (handler.canEnchant(client.player, true) && isInRightButtonBounds(posX, posY, (int) mouseX, (int) mouseY) && handler.onButtonClick(client.player, 2)) {
			client.interactionManager.clickButton(handler.syncId, 2);
			client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1));
			nextPageAngle += 1;
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	private void renderBook(MatrixStack matrices, float delta) {
		int scaleFactor = (int) client.getWindow().getScaleFactor();
		float deltaTurningSpeed = MathHelper.lerp(delta, pageTurningSpeed, nextPageTurningSpeed);
		float leftFlipAmount = MathHelper.lerp(delta, pageAngle, nextPageAngle) + 0.25F;
		float rightFlipAmount = MathHelper.lerp(delta, pageAngle, nextPageAngle) + 0.75F;
		DiffuseLighting.disableGuiDepthLighting();
		RenderSystem.backupProjectionMatrix();
		RenderSystem.viewport((width - 320) / 2 * scaleFactor, (height - 240) / 2 * scaleFactor, 320 * scaleFactor, 240 * scaleFactor);
		Matrix4f matrix4f = Matrix4f.translate(-0.34F, 0.23F, 0);
		matrix4f.multiply(Matrix4f.viewboxMatrix(90.0, 4 / 3F, 9, 80));
		RenderSystem.setProjectionMatrix(matrix4f);
		matrices.push();
		matrices.translate(0.0, 3.3F, 1984);
		matrices.scale(5, 5, 5);
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(20));
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-(1 - deltaTurningSpeed) * 90 - 90));
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180));
		bookModel.setPageAngles(0, MathHelper.clamp((leftFlipAmount - (float) MathHelper.fastFloor(leftFlipAmount)) * 1.6F - 0.3F, 0, 1), MathHelper.clamp((rightFlipAmount - (float) MathHelper.fastFloor(rightFlipAmount)) * 1.6F - 0.3F, 0, 1), deltaTurningSpeed);
		VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
		bookModel.render(matrices, immediate.getBuffer(bookModel.getLayer(BOOK_TEXTURE)), 0xF000F0, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
		immediate.draw();
		matrices.pop();
		RenderSystem.viewport(0, 0, client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
		RenderSystem.restoreProjectionMatrix();
		DiffuseLighting.enableGuiDepthLighting();
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
