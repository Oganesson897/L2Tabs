package dev.xkmc.l2tabs.tabs.core;

import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class TabGroup<G extends TabGroupData<G>> {

	public final static TabSprites UP = new TabSprites(26, 32,
			L2Tabs.loc("up/des_0"),
			L2Tabs.loc("up/des_1"),
			L2Tabs.loc("up/des_2"),
			L2Tabs.loc("up/sel_0"),
			L2Tabs.loc("up/sel_1"),
			L2Tabs.loc("up/sel_2")
	);

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
	public synchronized <T extends TabBase<G, T>> TabToken<G, T> registerTab(int priority, Supplier<TabToken.TabFactory<G, T>> sup, Component title) {
		TabToken<G, T> ans = new TabToken<>(this, sup, title);
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

	public TabSprites texture() {
		return UP;
	}
}
