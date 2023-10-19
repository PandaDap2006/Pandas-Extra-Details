package me.pandamods.extra_details.mixin.pandalib.client;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderList;
import me.jellysquid.mods.sodium.client.render.chunk.lists.SortedRenderLists;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.util.iterator.ByteArrayIterator;
import me.jellysquid.mods.sodium.client.util.iterator.ByteIterator;
import me.jellysquid.mods.sodium.client.world.WorldRendererExtended;
import me.pandamods.extra_details.mixin.pandalib.sodium.SodiumWorldRendererAccessor;
import me.pandamods.pandalib.client.render.block.BlockRendererDispatcher;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.mixin_extensions.CompileResultsExtension;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Shadow @Final private RenderBuffers renderBuffers;

	@Shadow @Nullable private ClientLevel level;

	@Shadow @Final private ObjectArrayList<LevelRenderer.RenderChunkInfo> renderChunksInFrustum;

	@Shadow protected abstract void checkPoseStack(PoseStack poseStack);

	@Inject(
			method = "renderLevel",
			at = @At(
					value = "INVOKE",
					target = "Lit/unimi/dsi/fastutil/longs/Long2ObjectMap;long2ObjectEntrySet()Lit/unimi/dsi/fastutil/objects/ObjectSet;",
					shift = At.Shift.BY, by = -2
			), locals = LocalCapture.CAPTURE_FAILEXCEPTION
	)
	public void renderLevel(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera,
							GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
		ProfilerFiller profilerfiller = this.level.getProfiler();
		MultiBufferSource.BufferSource buffersource = this.renderBuffers.bufferSource();

		profilerfiller.popPush("clientblocks");
		List<ClientBlock> clientBlocks = new ArrayList<>();
		if (this.level != null && !this.renderChunksInFrustum.isEmpty()) {
			for (LevelRenderer.RenderChunkInfo renderChunkInfo : this.renderChunksInFrustum) {
				clientBlocks.addAll(((CompileResultsExtension) renderChunkInfo.chunk.getCompiledChunk()).getBlocks());
			}
		}

		Vec3 vec3 = camera.getPosition();
		for (ClientBlock clientBlock : clientBlocks) {
			BlockState state = clientBlock.getBlockState();
			BlockPos pos = clientBlock.getBlockPos();

			MultiBufferSource buffer = this.renderBuffers.bufferSource();
			poseStack.pushPose();
			poseStack.translate(pos.getX() - vec3.x, pos.getY() - vec3.y, pos.getZ() - vec3.z);
			BlockRendererDispatcher.render(this.level, poseStack, buffer, clientBlock,
					LevelRenderer.getLightColor(this.level, state, pos), OverlayTexture.NO_OVERLAY, partialTick);
			poseStack.popPose();
		}

		this.checkPoseStack(poseStack);
		buffersource.endBatch(RenderType.solid());
		buffersource.endBatch(RenderType.entitySolid(TextureAtlas.LOCATION_BLOCKS));
		buffersource.endBatch(RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS));
		buffersource.endBatch(RenderType.entityCutoutNoCull(TextureAtlas.LOCATION_BLOCKS));
		buffersource.endBatch(RenderType.entitySmoothCutout(TextureAtlas.LOCATION_BLOCKS));
		this.renderBuffers.outlineBufferSource().endOutlineBatch();
	}
}
