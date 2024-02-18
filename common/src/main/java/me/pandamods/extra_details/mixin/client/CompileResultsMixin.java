package me.pandamods.extra_details.mixin.client;

import me.pandamods.extra_details.api.impl.CompileResultsExtension;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

@Mixin(ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults.class)
public class CompileResultsMixin implements CompileResultsExtension {
	@Unique
	private Set<BlockPos> blocks = new HashSet<>();

	@Override
	public Set<BlockPos> getRenderableBlocks() {
		return blocks;
	}
}
