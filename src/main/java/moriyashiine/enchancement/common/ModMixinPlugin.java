/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ModMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.contains("integration.architectury")) {
			return FabricLoader.getInstance().isModLoaded("architectury");
		} else if (mixinClassName.contains("integration.geckolib")) {
			return FabricLoader.getInstance().isModLoaded("geckolib");
		} else if (mixinClassName.contains("integration.sodium")) {
			return FabricLoader.getInstance().isModLoaded("sodium");
		} else if (mixinClassName.contains("integration.spectrum")) {
			if (FabricLoader.getInstance().isModLoaded("spectrum")) {
				if (mixinClassName.contains("SpectrumResourceConditionsMixin")) {
					ModContainer container = FabricLoader.getInstance().getModContainer("spectrum").orElse(null);
					if (container != null) {
						String[] version = container.getMetadata().getVersion().getFriendlyString().replace(".", "").split("");
						if (Integer.parseInt(version[version.length - 1]) > 7) {
							return true;
						} else {
							Enchancement.LOGGER.warn("[{}] Skipping SpectrumResourceConditionsMixin since Spectrum hasn't updated yet. This may cause world loading log spam and cause certain recipes to fail to load, but it will be fixed whenever Spectrum updates.", Enchancement.MOD_ID);
							return false;
						}
					}
				}
				return true;
			}
			return false;
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
