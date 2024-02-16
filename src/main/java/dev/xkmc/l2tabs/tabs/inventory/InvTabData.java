package dev.xkmc.l2tabs.tabs.inventory;

import dev.xkmc.l2tabs.init.data.L2TabsConfig;
import dev.xkmc.l2tabs.tabs.core.TabGroupData;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.network.chat.contents.TranslatableContents;

public class InvTabData extends TabGroupData<InvTabData> {

	public InvTabData() {
		super(TabRegistry.GROUP);
	}

	@Override
	public boolean shouldRender() {
		return L2TabsConfig.CLIENT.showTabs.get();
	}

	@Override
	public <X extends TabGroupData<X>> boolean shouldHideTab(TabToken<X, ?> e) {
		return e.title.getContents() instanceof TranslatableContents tr &&
				L2TabsConfig.CLIENT.hiddenTabs.get().contains(tr.getKey());
	}
}
