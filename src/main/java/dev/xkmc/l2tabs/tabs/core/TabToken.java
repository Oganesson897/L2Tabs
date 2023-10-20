package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class TabToken<G extends TabGroupData<G>, T extends TabBase<G, T>> {

	public interface TabFactory<G extends TabGroupData<G>, T extends TabBase<G, T>> {

		T create(int index, TabToken<G, T> token, TabManager<G> manager, ItemStack stack, Component component);

	}

	private final TabGroup<G> group;
	private final TabFactory<G, T> factory;
	private final Supplier<Item> item;
	private final Component title;

	int index;

	public TabToken(TabGroup<G> group, TabFactory<G, T> factory, Supplier<Item> item, Component component) {
		this.group = group;
		this.factory = factory;

		this.item = item;
		this.title = component;
	}

	public int getIndex() {
		group.refreshIndex();
		return index;
	}

	public TabType getType() {
		return group.type;
	}

	public T create(int index, TabManager<G> manager) {
		return factory.create(index, this, manager, item.get().getDefaultInstance(), title);
	}

}
