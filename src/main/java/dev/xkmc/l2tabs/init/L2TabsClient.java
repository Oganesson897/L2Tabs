package dev.xkmc.l2tabs.init;

import dev.xkmc.l2tabs.compat.TabCuriosCompat;
import dev.xkmc.l2tabs.init.data.L2TabsLangData;
import dev.xkmc.l2tabs.tabs.contents.TabAttributes;
import dev.xkmc.l2tabs.tabs.contents.TabInventory;
import dev.xkmc.l2tabs.tabs.core.TabGroup;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.core.TabType;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = L2Tabs.MODID, bus = EventBusSubscriber.Bus.MOD)
public class L2TabsClient {

	public static final TabGroup<InvTabData> GROUP = new TabGroup<>(TabType.ABOVE);

	public static TabToken<InvTabData, TabInventory> TAB_INVENTORY;
	public static TabToken<InvTabData, TabAttributes> TAB_ATTRIBUTE;

	@SubscribeEvent
	public static void client(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			TAB_INVENTORY = GROUP.registerTab(0, TabInventory::new, () -> Items.CRAFTING_TABLE, L2TabsLangData.INVENTORY.get());
			TAB_ATTRIBUTE = GROUP.registerTab(1000, TabAttributes::new, () -> Items.IRON_SWORD, L2TabsLangData.ATTRIBUTE.get());
			TabCuriosCompat.onClientInit();
		});
	}

}
