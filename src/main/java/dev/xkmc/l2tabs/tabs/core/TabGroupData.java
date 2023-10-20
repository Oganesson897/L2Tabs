package dev.xkmc.l2tabs.tabs.core;

import dev.xkmc.l2serial.util.Wrappers;

import java.util.List;

public class TabGroupData<G extends TabGroupData<G>> {

	private final TabGroup<G> group;

	public TabGroupData(TabGroup<G> group) {
		this.group = group;
	}


	public TabGroup<G> getGroup() {
		return group;
	}

	public boolean shouldRender() {
		return true;
	}

	public boolean allows(TabToken<G, ?> tab) {
		return true;
	}

	public G getThis() {
		return Wrappers.cast(this);
	}

	public List<TabToken<G, ?>> getTabs() {
		return group.getTabs(getThis());
	}

}
