package dev.xkmc.l2tabs.compat;

import dev.xkmc.l2tabs.tabs.core.ITabScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CuriosListScreen extends BaseCuriosListScreen<CuriosListMenu> implements ITabScreen {

	public CuriosListScreen(CuriosListMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	public void init() {
		super.init();
		new TabManager<>(this, new InvTabData()).init(this::addRenderableWidget, TabCurios.tab);
	}

}
