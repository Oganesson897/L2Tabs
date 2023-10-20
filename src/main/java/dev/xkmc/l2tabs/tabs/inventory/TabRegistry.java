package dev.xkmc.l2tabs.tabs.inventory;

import dev.xkmc.l2tabs.tabs.core.TabGroup;
import dev.xkmc.l2tabs.tabs.core.TabType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TabRegistry {

	public static final TabGroup<InvTabData> GROUP = new TabGroup<>(TabType.ABOVE);

}
