package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
		int guiLeft, guiTop;
		if (screen instanceof BaseTextScreen tx) {
			guiLeft = tx.leftPos;
			guiTop = tx.topPos;
		} else if (screen instanceof AbstractContainerScreen<?> tx) {
			guiLeft = tx.getGuiLeft();
			guiTop = tx.getGuiTop();
		} else {
			guiLeft = (screen.width - 176) / 2;
			guiTop = (screen.height - 166) / 2;
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
			tab.setX(guiLeft + xpos * 26);
			tab.setY(guiTop - 28);
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

		adder.accept(left = Button.builder(
				Component.literal("<"), b -> {
					tabPage = Math.max(tabPage - 1, 0);
					updateVisibility();
				}).bounds(guiLeft + radius, guiTop - 26 + radius,
				26 - radius * 2, 26 - radius * 2).build());

		adder.accept(right = Button.builder(
				Component.literal(">"), b -> {
					tabPage = Math.min(tabPage + 1, maxPages);
					updateVisibility();
				}).bounds(guiLeft + (TabType.MAX_TABS - 1) * 26 + radius, guiTop - 26 + radius,
				26 - radius * 2, 26 - radius * 2).build());

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

