package me.pandamods.extra_details.client.animation_controller;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.clientblockentity.LeverBlockEntity;
import me.pandamods.pandalib.client.armature.AnimationController;
import me.pandamods.pandalib.client.armature.Armature;
import me.pandamods.pandalib.client.armature.IAnimatableCache;
import net.minecraft.resources.ResourceLocation;
import org.joml.Math;

public class LeverAnimationController implements AnimationController<LeverBlockEntity> {
	@Override
	public ResourceLocation armatureLocation(LeverBlockEntity leverBlockEntity) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/armatures/block/redstone/lever.json");
	}

	@Override
	public void animate(LeverBlockEntity leverBlockEntity, Armature armature, float partialTick) {
		armature.getBone("handle").ifPresent(bone -> bone.localTransform.setRotationXYZ(Math.toRadians(-45), 0, 0));
	}
}