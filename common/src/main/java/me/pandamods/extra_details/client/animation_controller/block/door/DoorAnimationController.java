package me.pandamods.extra_details.client.animation_controller.block.door;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.DoorClientBlock;
import me.pandamods.extra_details.pandalib.client.animation_controller.Animation;
import me.pandamods.extra_details.pandalib.client.animation_controller.AnimationController;
import me.pandamods.extra_details.pandalib.client.model.Armature;
import me.pandamods.extra_details.pandalib.entity.MeshAnimatable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DoorBlock;

public class DoorAnimationController extends AnimationController<DoorClientBlock> {
	public <T extends MeshAnimatable> DoorAnimationController(DoorClientBlock base) {
		super(base);
		this.skipAnimation();
		this.setTransitionLength(0.1f);
		this.setAnimationSpeed(ExtraDetails.getConfig().blockSettings.door.animationSpeed);
	}

	private final Animation open = Animation.of(
			new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/door/door_open.json"));
	private final Animation close = Animation.of(
			new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/door/door_close.json"));

	@Override
	public Animation controller(DoorClientBlock base, Armature armature, float deltaSeconds) {
		return base.getBlockState().getValue(DoorBlock.OPEN) ? this.open : this.close;
	}
}
