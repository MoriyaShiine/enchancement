/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class ModPreLaunchEntrypoint implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		MixinExtrasBootstrap.init();
		MidnightConfig.init(Enchancement.MOD_ID, ModConfig.class);
	}
}
