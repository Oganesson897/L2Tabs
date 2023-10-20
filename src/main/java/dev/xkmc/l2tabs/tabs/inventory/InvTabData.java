package dev.xkmc.l2tabs.tabs.inventory;

import dev.xkmc.l2tabs.init.data.L2TabsConfig;
import dev.xkmc.l2tabs.tabs.core.TabGroupData;

public class InvTabData extends TabGroupData<InvTabData> {

	public InvTabData() {
		super(TabRegistry.GROUP);
	}

	@Override
	public boolean shouldRender() {
		return L2TabsConfig.CLIENT.showTabs.get();
	}
}
