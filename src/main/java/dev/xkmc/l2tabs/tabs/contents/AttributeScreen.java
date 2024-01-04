package dev.xkmc.l2tabs.tabs.contents;

import dev.xkmc.l2tabs.init.L2TabsClient;
import dev.xkmc.l2tabs.init.data.AttributeDisplayConfig;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class AttributeScreen extends BaseAttributeScreen {

	protected AttributeScreen(Component title, int page) {
		super(title, page);
	}

	@Override
	public void init() {
		super.init();
		new TabManager<>(this, new InvTabData()).init(this::addRenderableWidget, L2TabsClient.TAB_ATTRIBUTE);
	}

	protected void click(int nextPage) {
		Minecraft.getInstance().setScreen(new AttributeScreen(getTitle(), nextPage));
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float ptick) {
		super.render(g, mx, my, ptick);
		Player player = Minecraft.getInstance().player;
		assert player != null;
		render(g, mx, my, ptick, player, AttributeDisplayConfig.get());
	}

}