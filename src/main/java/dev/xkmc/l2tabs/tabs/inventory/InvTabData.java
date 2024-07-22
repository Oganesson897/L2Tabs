package dev.xkmc.l2tabs.tabs.inventory;

import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.init.data.L2TabsConfig;
import dev.xkmc.l2tabs.tabs.core.TabGroupData;
import dev.xkmc.l2tabs.tabs.core.TabToken;

public class InvTabData extends TabGroupData<InvTabData> {

	public InvTabData() {
		super(L2Tabs.GROUP);
	}

	@Override
	public boolean shouldRender() {
		return L2TabsConfig.CLIENT.showTabs.get();
	}

	@Override
	public <X extends TabGroupData<X>> boolean shouldHideTab(TabToken<X, ?> e) {
		var rl = L2Tabs.TABS.get().getKey(e);
		assert rl != null;
		return L2TabsConfig.CLIENT.hiddenTabs.get().contains(rl.getPath());
	}

}
