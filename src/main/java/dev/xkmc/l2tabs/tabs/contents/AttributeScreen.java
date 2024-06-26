package dev.xkmc.l2tabs.tabs.contents;

import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.l2tabs.init.L2TabsClient;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class AttributeScreen extends BaseAttributeScreen {

	protected AttributeScreen(Component title, int page) {
		super(title, page);
	}

	@Override
	public void init() {
		super.init();
		new TabManager<>(this, new InvTabData()).init(this::addRenderableWidget, L2TabsClient.TAB_ATTRIBUTE);
	}

	@Override
	public LivingEntity getEntity() {
		return minecraft.player;
	}

	protected void click(int nextPage) {
		Minecraft.getInstance().setScreen(new AttributeScreen(getTitle(), nextPage));
	}

}