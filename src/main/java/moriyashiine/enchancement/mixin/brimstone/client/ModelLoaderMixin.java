/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.brimstone.client;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedList;
import java.util.List;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	@Unique
	private static final Identifier CROSSBOW = new Identifier("item/crossbow");

	@Inject(method = "loadModelFromJson", at = @At("RETURN"))
	private void enchancement$brimstone(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
		if (id.equals(CROSSBOW)) {
			for (int i = 0; i < 6; i++) {
				List<ModelOverride.Condition> conditions = new LinkedList<>();
				conditions.add(new ModelOverride.Condition(new Identifier("charged"), 1));
				conditions.add(new ModelOverride.Condition(Enchancement.id("brimstone"), (i + 1) / 6F));
				cir.getReturnValue().getOverrides().add(new ModelOverride(Enchancement.id("item/crossbow_brimstone_" + i), conditions));
			}
		}
	}
}
