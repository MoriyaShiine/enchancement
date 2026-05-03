/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.enchancement.client.renderer.entity.model.MobSpinAttackEffectModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class MobSpinAttackEffectLayer<T extends LivingEntityRenderState> extends RenderLayer<T, EntityModel<T>> {
	private final MobSpinAttackEffectModel model;

	public MobSpinAttackEffectLayer(RenderLayerParent<T, EntityModel<T>> renderer, EntityModelSet modelSet) {
		super(renderer);
		model = new MobSpinAttackEffectModel(modelSet.bakeLayer(MobSpinAttackEffectModel.LAYER));
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, T state, float yRot, float xRot) {
		if (state.isAutoSpinAttack) {
			submitNodeCollector.submitModel(model, state, poseStack, SpinAttackEffectLayer.TEXTURE, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
		}
	}
}
