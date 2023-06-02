package dev.xkmc.l2tabs.init;

import dev.xkmc.l2tabs.compat.TabCuriosCompat;
import dev.xkmc.l2tabs.tabs.contents.TabAttributes;
import dev.xkmc.l2tabs.tabs.contents.TabInventory;
import dev.xkmc.l2tabs.tabs.core.TabRegistry;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = L2Tabs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2TabsClient {

	public static TabToken<TabInventory> TAB_INVENTORY;
	public static TabToken<TabAttributes> TAB_ATTRIBUTE;

	@SubscribeEvent
	public static void client(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			TAB_INVENTORY = TabRegistry.registerTab(0, TabInventory::new, () -> Items.CRAFTING_TABLE, L2TabsLangData.INVENTORY.get());
			TAB_ATTRIBUTE = TabRegistry.registerTab(1000, TabAttributes::new, () -> Items.IRON_SWORD, L2TabsLangData.ATTRIBUTE.get());
			TabCuriosCompat.onClientInit();
		});
	}

}
