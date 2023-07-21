package me.pandamods.extra_details.client.renderer.block.sign;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.utils.VectorUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;
import org.joml.Random;
import org.joml.Vector3f;

public class TiltSignRenderer extends SignRenderer {
	public TiltSignRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(SignBlockEntity signBlockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
		poseStack.pushPose();
		if (ExtraDetails.getConfig().enable_sign_tilt && !(signBlockEntity.getBlockState().getBlock() instanceof WallSignBlock)) {
			float tiltX = ExtraDetails.getConfig().sign_details.pitch_tilt;
			float tiltY = ExtraDetails.getConfig().sign_details.yaw_tilt;
			float tiltZ = ExtraDetails.getConfig().sign_details.roll_tilt;

			if (ExtraDetails.getConfig().sign_details.random_tilt) {
				float minX = -ExtraDetails.getConfig().sign_details.pitch_tilt;
				float maxX = ExtraDetails.getConfig().sign_details.pitch_tilt;
				tiltX = new Random(
						signBlockEntity.getBlockPos().asLong()).nextFloat() * (maxX - minX) + minX;

				float minY = -ExtraDetails.getConfig().sign_details.yaw_tilt;
				float maxY = ExtraDetails.getConfig().sign_details.yaw_tilt;
				tiltY = new Random(
						(long) (signBlockEntity.getBlockPos().asLong() - signBlockEntity.getBlockPos().asLong() / 2.5)).nextFloat() * (maxY - minY) + minY;

				float minZ = -ExtraDetails.getConfig().sign_details.roll_tilt;
				float maxZ = ExtraDetails.getConfig().sign_details.roll_tilt;
				tiltZ = new Random(
						(long) (signBlockEntity.getBlockPos().asLong() + signBlockEntity.getBlockPos().asLong() / 1.5)).nextFloat() * (maxZ - minZ) + minZ;
			}

			Vector3f rotation = new Vector3f(tiltX, tiltY, tiltZ);
			BlockState state = signBlockEntity.getBlockState();
			rotation.rotateY(Math.toRadians(((SignBlock) state.getBlock()).getYRotationDegrees(state)));

			VectorUtils.rotateByPivot(poseStack, new Vector3f(0.5f, 0, 0.5f), new Vector3f(
					Math.toRadians(rotation.x), Math.toRadians(rotation.y), Math.toRadians(rotation.z)));
		}
		super.render(signBlockEntity, f, poseStack, multiBufferSource, i, j);
		poseStack.popPose();
	}
}
