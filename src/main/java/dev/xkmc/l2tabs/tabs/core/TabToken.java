package dev.xkmc.l2tabs.tabs.core;

import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class TabToken<G extends TabGroupData<G>, T extends TabBase<G, T>> {

	public interface TabFactory<G extends TabGroupData<G>, T extends TabBase<G, T>> {

		T create(int index, TabToken<G, T> token, TabManager<G> manager, Component component);

	}

	private final TabGroup<G> group;
	private final Supplier<TabFactory<G, T>> factory;
	public final Component title;

	TabToken(TabGroup<G> group, Supplier<TabFactory<G, T>> factory, Component component) {
		this.group = group;
		this.factory = factory;
		this.title = component;
	}

	public TabType getType() {
		return group.type;
	}

	public T create(int index, TabManager<G> manager) {
		return factory.get().create(index, this, manager, title);
	}

	public void draw(GuiGraphics g, int x, int y, boolean selected, int index) {
		int ind = index == 0 ? 0 : group.enableLast() && index == group.max() ? 2 : 1;
		getType().draw(group.texture(), g, x, y, selected, ind);
	}

	public int getOrder() {
		var holder = L2Tabs.TABS.reg().wrapAsHolder(this);
		var ans = holder.getData(L2Tabs.ORDER.reg());
		return ans == null ? 0 : ans;
	}

}
