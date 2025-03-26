/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.render.entity.model;

import moriyashiine.enchancement.client.render.entity.state.FrozenPlayerEntityRenderState;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

public class FrozenPlayerEntityModel extends BipedEntityModel<FrozenPlayerEntityRenderState> {
	public static final EntityModelLayer LAYER = new EntityModelLayer(Enchancement.id("frozen_player"), "main");
	public static final EntityModelLayer LAYER_SLIM = new EntityModelLayer(Enchancement.id("frozen_player_slim"), "main");

	private final boolean slim;

	public FrozenPlayerEntityModel(ModelPart root, boolean slim) {
		super(root);
		this.slim = slim;
	}

	public static TexturedModelData getTexturedModelData(boolean slim) {
		ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData root = modelData.getRoot();
		root.getChild(EntityModelPartNames.HEAD).addChild(EntityModelPartNames.HAT, ModelPartBuilder.create(), ModelTransform.NONE);
		if (slim) {
			root.addChild(
					EntityModelPartNames.LEFT_ARM,
					ModelPartBuilder.create().uv(32, 48).cuboid(-1, -2, -2, 3, 12, 4, Dilation.NONE),
					ModelTransform.origin(5, 2, 0)
			);
			root.addChild(
					EntityModelPartNames.RIGHT_ARM,
					ModelPartBuilder.create().uv(40, 16).cuboid(-2, -2, -2, 3, 12, 4, Dilation.NONE),
					ModelTransform.origin(-5, 2, 0)
			);
		}
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		getRootPart().applyTransform(matrices);
		ModelPart armPart = getArm(arm);
		if (slim) {
			float origin = 0.5F * (arm == Arm.RIGHT ? 1 : -1);
			armPart.originX += origin;
			armPart.applyTransform(matrices);
			armPart.originX -= origin;
		} else {
			armPart.applyTransform(matrices);
		}
	}
}
