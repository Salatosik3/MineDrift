package io.github.salatosik3.minedrift.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineDriftClient implements ClientModInitializer {
	public static final String MOD_ID = "minedrift";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private float rotation = 0;

	@Override
	public void onInitializeClient() {

		HudElementRegistry.addFirst(Identifier.fromNamespaceAndPath(MOD_ID, "trying_to_figure_it_out"),
				(graphics, deltaTracker) -> {

			var pose = graphics.pose();
			pose.pushMatrix();

//			graphics.textRenderer().accept(graphics.guiWidth() / 2, graphics.guiHeight() / 2, Component.literal(String.valueOf(deltaTracker.getGameTimeDeltaPartialTick(true))));
//			graphics.textRenderer().accept(graphics.guiWidth() / 2, graphics.guiHeight() / 2, Component.literal("Go fuck yourself."));

			var centerX = graphics.guiWidth() / 2;
			var centerY = graphics.guiHeight() / 2;

			pose.translate(centerX, centerY);

			rotation += 0.01f;
			pose.rotate(rotation);

//			pose.scale(2, 2);

			graphics.fakeItem(new ItemStack(Items.ACACIA_BOAT, 1), 0, 0);
			pose.popMatrix();
		});

	}
}