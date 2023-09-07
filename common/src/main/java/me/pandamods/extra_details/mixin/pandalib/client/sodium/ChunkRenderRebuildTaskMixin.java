package me.pandamods.extra_details.mixin.pandalib.client.sodium;

import me.jellysquid.mods.sodium.client.gl.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderBuildTask;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import me.jellysquid.mods.sodium.client.util.task.CancellationSource;
import me.jellysquid.mods.sodium.client.world.cloned.ChunkRenderContext;
import me.pandamods.pandalib.client.render.block.BlockRendererRegistry;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.client.render.block.ClientBlockProvider;
import me.pandamods.pandalib.client.render.block.ClientBlockRegistry;
import me.pandamods.pandalib.mixin_extensions.CompileResultsExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ChunkRenderRebuildTask.class)
public abstract class ChunkRenderRebuildTaskMixin extends ChunkRenderBuildTask {
	@Shadow(remap = false)
    @Final
    private RenderSection render;

	@Shadow @Final private ChunkRenderContext renderContext;

	@Inject(method = "performBuild", at = @At("TAIL"), remap = false)
    public void injectRenderPositions(ChunkBuildContext context, CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildContext> cir) {
        CompileResultsExtension extension = (CompileResultsExtension) cir.getReturnValue().data;

        for (BlockPos pos : BlockPos.betweenClosed(this.render.getOriginX(), this.render.getOriginY(), this.render.getOriginZ(), this.render.getOriginX() + 16, this.render.getOriginY() + 16, this.render.getOriginZ() + 16)) {
            BlockState state = context.cache.getWorldSlice().getBlockState(pos);
            if (state.isAir())
                continue;
            if (BlockRendererDispatcher.shouldRender(state)) {
                extension.getBlocks().add(pos.immutable());
            }
        }

		for (BlockPos pos : BlockPos.betweenClosed(
				this.render.getChunkPos().origin(),
				this.render.getChunkPos().origin().offset(16, 16, 16)
		)) {
				BlockState state = Minecraft.getInstance().level.getBlockState(pos);
				if (state.isAir())
					continue;
				ClientBlockProvider blockProvider = ClientBlockRegistry.get(state.getBlock());
				if (blockProvider != null) {
					List<ClientBlock> compiledBlocks = ((CompileResultsExtension) ).getBlocks().stream().filter(
							clientBlock -> clientBlock.getBlockPos().equals(pos.immutable()) && clientBlock.getBlockState().is(state.getBlock())).toList();
					if (compiledBlocks.isEmpty()) {
						((CompileResultsExtension) (Object) compileResults).getBlocks().add(
							blockProvider.create(pos.immutable(), state, Minecraft.getInstance().level));
					} else {
						ClientBlock block = compiledBlocks.get(0);
						block.setBlockState(state);
						((CompileResultsExtension) (Object) compileResults).getBlocks().add(block);
					}
				}
			}
    }
}
