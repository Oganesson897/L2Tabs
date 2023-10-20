package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class TabGroup<G extends TabGroupData<G>> {

	private final Map<Integer, TabToken<G, ?>> map = new TreeMap<>();

	public final TabType type;

	public TabGroup(TabType type) {
		this.type = type;
	}

	/**
	 * 0 - Inventory
	 * 1000 - Attributes
	 * 2000 - Curios
	 * 3000 - Artifacts
	 */
	public synchronized <T extends TabBase<G, T>> TabToken<G, T> registerTab(int priority, TabToken.TabFactory<G, T> sup, Supplier<Item> item, Component title) {
		TabToken<G, T> ans = new TabToken<>(this, sup, item, title);
		while (map.containsKey(priority)) {
			priority++;
		}
		map.put(priority, ans);
		return ans;
	}

	public List<TabToken<G, ?>> getTabs(G token) {
		List<TabToken<G, ?>> ans = new ArrayList<>();
		for (var e : map.values()) {
			if (token.allows(e)) {
				ans.add(e);
			}
		}
		return ans;
	}

}
