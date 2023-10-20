package dev.xkmc.l2tabs.tabs.core;

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

}
