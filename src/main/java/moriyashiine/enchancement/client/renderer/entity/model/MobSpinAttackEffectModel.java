/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.renderer.entity.model;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class MobSpinAttackEffectModel extends EntityModel<LivingEntityRenderState> {
	public static final ModelLayerLocation LAYER = new ModelLayerLocation(Enchancement.id("mob_spin_attack_effect"), "main");

	private final ModelPart[] boxes = new ModelPart[2];

	public MobSpinAttackEffectModel(final ModelPart root) {
		super(root);
		for (int i = 0; i < 2; i++) {
			boxes[i] = root.getChild(boxName(i));
		}
	}

	private static String boxName(final int i) {
		return "box" + i;
	}

	public static LayerDefinition createLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		for (int i = 0; i < 2; i++) {
			float yOffset = -3.2F + 9.6F * (i + 1);
			float scale = 0.75F * (i + 1);
			root.addOrReplaceChild(boxName(i), CubeListBuilder.create().texOffs(0, 0).addBox(-8, -16 + yOffset, -8, 16, 32, 16), PartPose.ZERO.withScale(scale));
		}
		return LayerDefinition.create(mesh, 64, 64);
	}

	public void setupAnim(final LivingEntityRenderState state) {
		super.setupAnim(state);
		for (int i = 0; i < boxes.length; i++) {
			float angle = state.ageInTicks * -(45 + (i + 1) * 5);
			boxes[i].yRot = (float) Math.toRadians(Mth.wrapDegrees(angle));
		}
	}
}
