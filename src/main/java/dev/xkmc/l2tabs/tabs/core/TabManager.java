package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TabManager<G extends TabGroupData<G>> {

	protected final List<TabBase<G, ?>> list = new ArrayList<>();

	public final ITabScreen screen;
	public final G token;
	private Button left, right;

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
		List<TabToken<G, ?>> token_list = new ArrayList<>(token.getTabs());
		token_list.removeIf(token::shouldHideTab);
		list.clear();
		this.selected = selected;
		int imgWidth = screen.getXSize();
		int imgHeight = screen.getYSize();
		int index = 0, order = 0, page = 0;

		for (TabToken<G, ?> token : token_list) {
			if (index > 0 && order == 0) {
				order++;
			}
			if (token == selected)
				tabPage = page;
			TabBase<G, ?> tab = token.create(order, this);
			tab.page = page;
			tab.setXRef(screen::getGuiLeft, group.type.getTabX(imgWidth, order));
			tab.setYRef(screen::getGuiTop, group.type.getTabY(imgHeight, order));
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

		adder.accept(left = PageFlipButtons.getLeftButton(screen, b -> {
			tabPage = Math.max(tabPage - 1, 0);
			updateVisibility();
		}));
		adder.accept(right = PageFlipButtons.getRightButton(screen, b -> {
			tabPage = Math.min(tabPage + 1, maxPages);
			updateVisibility();
		}));

		updateVisibility();
	}

	private void updateVisibility() {
		left.visible = left.active = tabPage > 0;
		right.visible = right.active = tabPage < maxPages - 1;
		for (TabBase<G, ?> tab : list) {
			tab.visible = tab.page == tabPage;
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

