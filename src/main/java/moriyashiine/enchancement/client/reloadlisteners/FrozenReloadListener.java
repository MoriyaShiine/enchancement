/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.client.reloadlisteners;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author UpcraftLP (<a href="https://github.com/UpcraftLP">https://github.com/UpcraftLP</a>)
 */
@Environment(EnvType.CLIENT)
public class FrozenReloadListener implements IdentifiableResourceReloadListener, SimpleSynchronousResourceReloadListener {
	public static final FrozenReloadListener INSTANCE = new FrozenReloadListener();

	private static final Identifier ID = new Identifier(Enchancement.MOD_ID, "frozen");

	private static final Identifier PACKED_ICE_TEXTURE = new Identifier("textures/block/packed_ice.png");
	private static final boolean DEBUG_TEXTURES = Boolean.getBoolean(Enchancement.MOD_ID + ".debug_frozen_textures");

	private final Map<Identifier, Identifier> TEXTURE_CACHE = new HashMap<>();

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	@Override
	public void reload(ResourceManager manager) {
		// make sure resourcepack changes affect frozen entities
		TEXTURE_CACHE.clear();
	}

	@Override
	public Collection<Identifier> getFabricDependencies() {
		// make sure textures are fully reloaded before we regenerate our cached textures
		return Collections.singleton(ResourceReloadListenerKeys.TEXTURES);
	}

	public Identifier getTexture(Identifier original) {
		// generate a suitably sized texture on the fly, if it doesn't exist in cache already
		return TEXTURE_CACHE.computeIfAbsent(original, id -> {
			ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
			try (NativeImage tex = loadNative(resourceManager, id)) {
				return generateTexture(resourceManager, tex.getWidth(), tex.getHeight());
			} catch (IOException e) {
				Enchancement.LOGGER.warn("Unable to generate frozen texture for " + original, e);
				return original;
			}
		});
	}

	/**
	 * create a new texture of width x height by repeating the source texture in a grid-like fashion
	 */
	private static Identifier generateTexture(ResourceManager resourceManager, int texWidth, int texHeight) throws IOException {
		try (NativeImage srcTex = loadNative(resourceManager, PACKED_ICE_TEXTURE)) {
			if (srcTex.getWidth() == 0 || srcTex.getHeight() == 0) {
				throw new IllegalStateException(String.format("bad resourcepack, texture for %s was %sx%s, this is not allowed!", PACKED_ICE_TEXTURE, srcTex.getWidth(), srcTex.getHeight()));
			}
			int width = texWidth;
			int height = texHeight;
			// only apply to properly scaled textures
			if (texWidth % 16 == 0 && texHeight % 16 == 0) {
				// if there is a resource pack with larger than 16x textures, scale the result texture resolution accordingly
				width = srcTex.getWidth() * (texWidth / 16);
				height = srcTex.getHeight() * (texHeight / 16);
			}
			NativeImage destTex = new NativeImage(width, height, false);
			// manually write each pixel of the target texture
			// this bypasses having to deal with shaders, and should™ be fine for reasonably sized textures
			// (TL;DR: if someone has a 16384x16384 resource pack they're expected to have a computer that can copy a
			// few of these textures on the CPU just fine)
			for (int dx = 0; dx < width; dx++) {
				for (int dy = 0; dy < height; dy++) {
					destTex.setColor(dx, dy, srcTex.getColor(dx % srcTex.getWidth(), dy % srcTex.getHeight()));
				}
			}
			Identifier textureID = new Identifier(Enchancement.MOD_ID, String.format("textures/generated/frozen_%sx%s", width, height));
			// if in debug mode, output generated textures to the current game directory
			if (DEBUG_TEXTURES) {
				try {
					Path dir = FabricLoader.getInstance().getGameDir().resolve(Enchancement.MOD_ID + "_debug");
					Files.createDirectories(dir);
					Path output = dir.resolve(String.format("frozen_%sx%s.png", width, height));
					destTex.writeTo(output);
				} catch (IOException e) {
					// print stacktrace but keep the game running
					e.printStackTrace();
				}
			}
			MinecraftClient.getInstance().getTextureManager().registerTexture(textureID, new NativeImageBackedTexture(destTex));
			return textureID;
		}
	}

	/**
	 * load the NativeImage for a given MC texture
	 */
	private static NativeImage loadNative(ResourceManager resourceManager, Identifier identifier) throws IOException {
		return ResourceTexture.TextureData.load(resourceManager, identifier).getImage();
	}
}
