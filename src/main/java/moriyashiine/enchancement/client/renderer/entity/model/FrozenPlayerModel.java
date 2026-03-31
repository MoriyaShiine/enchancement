/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.renderer.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.enchancement.client.renderer.entity.state.FrozenPlayerEntityRenderState;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

public class FrozenPlayerModel extends HumanoidModel<FrozenPlayerEntityRenderState> {
	public static final ModelLayerLocation LAYER = new ModelLayerLocation(Enchancement.id("frozen_player"), "main");
	public static final ModelLayerLocation LAYER_SLIM = new ModelLayerLocation(Enchancement.id("frozen_player_slim"), "main");

	private final boolean slim;

	public FrozenPlayerModel(ModelPart root, boolean slim) {
		super(root);
		this.slim = slim;
	}

	public static LayerDefinition createBodyLayer(boolean slim) {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = mesh.getRoot();
		root.getChild(PartNames.HEAD).addOrReplaceChild(PartNames.HAT, CubeListBuilder.create(), PartPose.ZERO);
		if (slim) {
			root.addOrReplaceChild(
					PartNames.LEFT_ARM,
					CubeListBuilder.create().texOffs(32, 48).addBox(-1, -2, -2, 3, 12, 4, CubeDeformation.NONE),
					PartPose.offset(5, 2, 0)
			);
			root.addOrReplaceChild(
					PartNames.RIGHT_ARM,
					CubeListBuilder.create().texOffs(40, 16).addBox(-2, -2, -2, 3, 12, 4, CubeDeformation.NONE),
					PartPose.offset(-5, 2, 0)
			);
		}
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void translateToHand(FrozenPlayerEntityRenderState state, HumanoidArm arm, PoseStack poseStack) {
		root().translateAndRotate(poseStack);
		ModelPart armPart = getArm(arm);
		if (slim) {
			float origin = 0.5F * (arm == HumanoidArm.RIGHT ? 1 : -1);
			armPart.x += origin;
			armPart.translateAndRotate(poseStack);
			armPart.x -= origin;
		} else {
			armPart.translateAndRotate(poseStack);
		}
	}
}
