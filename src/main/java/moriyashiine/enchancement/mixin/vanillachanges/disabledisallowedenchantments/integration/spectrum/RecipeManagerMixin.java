/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments.integration.spectrum;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
	@Shadow
	@Final
	private RegistryWrapper.WrapperLookup registryLookup;

	@Shadow
	protected static RecipeEntry<?> deserialize(Identifier id, JsonObject json, RegistryWrapper.WrapperLookup registryLookup) {
		throw new UnsupportedOperationException();
	}

	@ModifyVariable(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("HEAD"), argsOnly = true)
	private Map<Identifier, JsonElement> enchancement$spectrum$disableDisallowedEnchantments(Map<Identifier, JsonElement> value) {
		Set<Identifier> toRemove = new HashSet<>();
		for (Map.Entry<Identifier, JsonElement> entry : value.entrySet()) {
			Identifier identifier = entry.getKey();
			try {
				RecipeEntry<?> recipe = deserialize(identifier, JsonHelper.asObject(entry.getValue(), "top element"), registryLookup);
				if (EnchancementUtil.ignoreRecipe(recipe)) {
					toRemove.add(identifier);
				}
			} catch (Exception ignored) {
			}
		}
		toRemove.forEach(value::remove);
		return value;
	}
}
