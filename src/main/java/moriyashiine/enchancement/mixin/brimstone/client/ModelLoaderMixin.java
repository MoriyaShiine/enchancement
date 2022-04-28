package moriyashiine.enchancement.mixin.brimstone.client;

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
	private void enchancement$brimstone(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
		if (id.equals(CROSSBOW)) {
			List<ModelOverride.Condition> conditions = new LinkedList<>();
			conditions.add(new ModelOverride.Condition(new Identifier(Enchancement.MOD_ID, "crossbow_brimstone"), 1));
			cir.getReturnValue().getOverrides().add(new ModelOverride(new Identifier(Enchancement.MOD_ID, "item/crossbow_brimstone"), conditions));
		}
	}
}
