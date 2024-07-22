package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class TabToken<G extends TabGroupData<G>, T extends TabBase<G, T>> {

	public interface TabFactory<G extends TabGroupData<G>, T extends TabBase<G, T>> {

		T create(int index, TabToken<G, T> token, TabManager<G> manager, ItemStack stack, Component component);

	}

	private final TabGroup<G> group;
	private final Supplier<TabFactory<G, T>> factory;
	private final Supplier<Item> item;
	public final Component title;

	TabToken(TabGroup<G> group, Supplier<TabFactory<G, T>> factory, Supplier<Item> item, Component component) {
		this.group = group;
		this.factory = factory;

		this.item = item;
		this.title = component;
	}

	public TabType getType() {
		return group.type;
	}

	public T create(int index, TabManager<G> manager) {
		return factory.get().create(index, this, manager, item.get().getDefaultInstance(), title);
	}

	public void draw(GuiGraphics g, int x, int y, boolean selected, int index) {
		getType().draw(group.texture(), g, x, y, selected, index);
	}

}
