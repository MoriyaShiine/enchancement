package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class GrapplingFishingBobberComponent implements AutoSyncedComponent {
	private final FishingHook obj;
	private float strength = 0;

	private Vec3 grapplePos = null;
	private BlockPos grappleBlockPos = null;
	private BlockState grappleState = null;

	public GrapplingFishingBobberComponent(FishingHook obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		strength = input.getFloatOr("Strength", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putFloat("Strength", strength);
	}

	public void sync() {
		EnchancementEntityComponents.GRAPPLING_FISHING_BOBBER.sync(obj);
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	public Vec3 getGrapplePos() {
		return grapplePos;
	}

	public void setGrapplePos(Vec3 grapplePos) {
		this.grapplePos = grapplePos;
	}

	public BlockPos getGrappleBlockPos() {
		return grappleBlockPos;
	}

	public void setGrappleBlockPos(BlockPos grappleBlockPos) {
		this.grappleBlockPos = grappleBlockPos;
	}

	public BlockState getGrappleState() {
		return grappleState;
	}

	public void setGrappleState(BlockState grappleState) {
		this.grappleState = grappleState;
	}
}
