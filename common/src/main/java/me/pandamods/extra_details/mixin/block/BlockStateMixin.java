package me.pandamods.extra_details.mixin.block;

import me.pandamods.extra_details.api.client.render.block.BlockRendererRegistry;
import me.pandamods.extra_details.api.client.render.block.ClientBlock;
import me.pandamods.extra_details.api.client.render.block.ClientBlockRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateMixin {
	@Shadow public abstract Block getBlock();

	@Shadow protected abstract BlockState asState();

	@Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
	public void getRenderShape(CallbackInfoReturnable<RenderShape> cir) {
		ClientBlockRenderer<ClientBlock> renderer = BlockRendererRegistry.get(this.getBlock());
		if (renderer != null && renderer.enabled(this.asState())) {
			RenderShape renderShape = renderer.getRenderShape(this.asState());
			if (renderShape != null)
				cir.setReturnValue(renderShape);
		}
	}
}
