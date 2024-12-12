/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.render.entity.state;

import net.minecraft.util.Arm;

public interface ArmedEntityRenderStateAddition {
	int enchancement$getRageColor(Arm arm);

	void enchancement$setRageColor(Arm arm, int color);
}
