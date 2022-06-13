package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import com.google.gson.JsonElement;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Tag.Builder.class)
public class TagGroupLoaderMixin {
	@ModifyVariable(method = "resolveEntry", at = @At("STORE"))
	private static boolean enchancement$preventTagsFromAddingRemovedEnchantments(boolean value, JsonElement json) {
		String string = json.isJsonObject() ? JsonHelper.getString(json.getAsJsonObject(), "id") : JsonHelper.asString(json, "id");
		if (!string.startsWith("#") && !Enchancement.getConfig().isEnchantmentAllowed(string) && Registry.ENCHANTMENT.get(new Identifier(string)) != null) {
			return false;
		}
		return value;
	}
}
