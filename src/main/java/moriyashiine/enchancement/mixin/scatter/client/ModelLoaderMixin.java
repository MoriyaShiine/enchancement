/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.scatter.client;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(EnvType.CLIENT)
@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	@Unique
	private static final Identifier CROSSBOW = new Identifier("item/crossbow");

	@Inject(method = "loadModelFromJson", at = @At("RETURN"))
	private void enchancement$scatter(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
		if (id.equals(CROSSBOW)) {
			List<ModelOverride.Condition> conditions = new LinkedList<>();
			conditions.add(new ModelOverride.Condition(Enchancement.id("crossbow_amethyst"), 1));
			cir.getReturnValue().getOverrides().add(new ModelOverride(Enchancement.id("item/crossbow_amethyst"), conditions));
		}
	}
}
