package dev.xkmc.l2tabs.tabs.core;

import dev.xkmc.l2tabs.init.data.L2TabsConfig;
import dev.xkmc.l2tabs.tabs.contents.BaseTextScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

public class TabManager {

	private final List<BaseTab<?>> list = new ArrayList<>();
	private final Screen screen;

	private Button left, right;

	public int tabPage, maxPages;
	public TabToken<?> selected;

	public TabManager(Screen screen) {
		this.screen = screen;
	}

	public void init(Consumer<AbstractWidget> adder, TabToken<?> selected) {
		list.clear();
		if (!L2TabsConfig.CLIENT.showTabs.get())
			return;
		this.selected = selected;
		IntSupplier guiLeft, guiTop;
		if (screen instanceof BaseTextScreen tx) {
			guiLeft = () -> tx.leftPos;
			guiTop = () -> tx.topPos;
		} else if (screen instanceof AbstractContainerScreen<?> tx) {
			guiLeft = tx::getGuiLeft;
			guiTop = tx::getGuiTop;
		} else {
			guiLeft = () -> (screen.width - 176) / 2;
			guiTop = () -> (screen.height - 166) / 2;
		}
		int index = 0, order = 0, page = 0;
		int radius = 3;
		for (TabToken<?> token : TabRegistry.getTabs()) {
			if (index > 0 && order == 0) {
				order++;
			}
			int xpos = order;

			if (token == selected)
				tabPage = page;
			BaseTab<?> tab = token.create(this);
			tab.page = page;
			tab.setXRef(guiLeft, xpos * 26);
			tab.setYRef(guiTop, -28);
			list.add(tab);
			adder.accept(tab);

			order++;
			if (order == TabType.MAX_TABS - 1) {
				order = 0;
				page++;
			}
			index++;
		}

		maxPages = order == 0 ? page : page + 1;

		adder.accept(left = new FloatingButton(guiLeft, guiTop, radius, -26 + radius,
				26 - radius * 2, 26 - radius * 2,
				Component.literal("<"), b -> {
			tabPage = Math.max(tabPage - 1, 0);
			updateVisibility();
		}));

		adder.accept(right = new FloatingButton(guiLeft, guiTop, (TabType.MAX_TABS - 1) * 26 + radius, -26 + radius,
				26 - radius * 2, 26 - radius * 2,
				Component.literal(">"), b -> {
			tabPage = Math.min(tabPage + 1, maxPages);
			updateVisibility();
		}));

		updateVisibility();
	}

	private void updateVisibility() {
		left.visible = left.active = tabPage > 0;
		right.visible = right.active = tabPage < maxPages - 1;
		for (BaseTab<?> tab : list) {
			tab.active = tab.visible = tab.page == tabPage;
		}
	}

	public Screen getScreen() {
		return screen;
	}

	public void onToolTipRender(GuiGraphics g, int mouseX, int mouseY) {
		for (BaseTab<?> tab : list) {
			if (tab.visible && tab.isHoveredOrFocused()) {
				tab.onTooltip(g, mouseX, mouseY);
			}
		}
	}
}
