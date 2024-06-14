package me.pandamods.extra_details;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import me.pandamods.extra_details.api.render.BlockRendererRegistry;
import me.pandamods.extra_details.client.renderer.DoorRenderer;
import me.pandamods.extra_details.client.renderer.LeverRenderer;
import me.pandamods.pandalib.client.render.PLInternalShaders;
import me.pandamods.pandalib.resource.AssimpResources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;

public class ExtraDetails {
	public static final String MOD_ID = "extra_details";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final ExtraDetailsLevelRenderer LEVEL_RENDERER = new ExtraDetailsLevelRenderer();

	public static void init() {
		ClientReloadShadersEvent.EVENT.register(PLInternalShaders::register);
		ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new AssimpResources(), new ResourceLocation(MOD_ID, "assimp_loader"));

		BlockRendererRegistry.register(new LeverRenderer(), Blocks.LEVER);
		BlockRendererRegistry.register(new DoorRenderer(), Blocks.OAK_DOOR, Blocks.DARK_OAK_DOOR, Blocks.ACACIA_DOOR, Blocks.BAMBOO_DOOR, Blocks.BIRCH_DOOR,
				Blocks.CHERRY_DOOR, Blocks.CRIMSON_DOOR, Blocks.WARPED_DOOR, Blocks.JUNGLE_DOOR, Blocks.SPRUCE_DOOR, Blocks.MANGROVE_DOOR, Blocks.IRON_DOOR);
	}
}