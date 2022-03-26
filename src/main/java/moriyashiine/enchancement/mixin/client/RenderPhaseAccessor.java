package moriyashiine.enchancement.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(RenderPhase.class)
public interface RenderPhaseAccessor {
	@Accessor("TEXT_INTENSITY_SHADER")
	static RenderPhase.Shader enchancement$getTranslucentShader() {
		throw new UnsupportedOperationException();
	}
}
