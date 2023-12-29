/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class ModPreLaunchEntrypoint implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		MixinExtrasBootstrap.init();
	}
}
