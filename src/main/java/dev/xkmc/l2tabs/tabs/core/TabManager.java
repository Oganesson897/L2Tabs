package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TabManager<G extends TabGroupData<G>> {

	protected final List<TabBase<G, ?>> list = new ArrayList<>();

	public final ITabScreen screen;
	public final G token;

	public int tabPage;
	public TabToken<G, ?> selected;

	private int maxPages = 0;

	public TabManager(ITabScreen screen, G token) {
		this.screen = screen;
		this.token = token;
	}

	public void init(Consumer<AbstractWidget> adder, TabToken<G, ?> selected) {
		if (!token.shouldRender()) return;
		TabGroup<G> group = token.getGroup();
		List<TabToken<G, ?>> token_list = token.getTabs();
		list.clear();
		this.selected = selected;
		int guiLeft = screen.getGuiLeft();
		int guiTop = screen.getGuiTop();
		int imgWidth = screen.getXSize();
		int imgHeight = screen.getYSize();
		for (int i = 0; i < token_list.size(); i++) {
			TabToken<G, ?> token = token_list.get(i);
			TabBase<G, ?> tab = token.create(i, this);
			tab.setX(guiLeft + group.type.getTabX(imgWidth, tab.index));
			tab.setY(guiTop + group.type.getTabY(imgHeight, tab.index));
			adder.accept(tab);
			list.add(tab);
		}

		int max = group.type.max;
		if (token_list.size() > max) {
			adder.accept(group.type.getLeftButton(screen, b -> {
				tabPage = Math.max(tabPage - 1, 0);
				updateVisibility();
			}));
			adder.accept(group.type.getRightButton(screen, b -> {
				tabPage = Math.min(tabPage + 1, maxPages);
				updateVisibility();
			}));
			maxPages = token_list.size() / max;
		}
		updateVisibility();
	}

	private void updateVisibility() {
		int max = token.getGroup().type.max;
		for (TabBase<G, ?> tab : list) {
			tab.visible = tab.index >= tabPage * max && tab.index < (tabPage + 1) * max;
			tab.active = tab.visible;
		}
	}

	public Screen getScreen() {
		return screen.asScreen();
	}

	public void onToolTipRender(GuiGraphics stack, int mouseX, int mouseY) {
		for (TabBase<G, ?> tab : list) {
			if (tab.visible && tab.isHoveredOrFocused()) {
				tab.onTooltip(stack, mouseX, mouseY);
			}
		}
	}

}
