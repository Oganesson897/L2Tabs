package dev.xkmc.l2tabs.init;

import dev.xkmc.l2tabs.compat.TabCuriosCompat;
import dev.xkmc.l2tabs.init.data.L2TabsLangData;
import dev.xkmc.l2tabs.tabs.contents.TabAttributes;
import dev.xkmc.l2tabs.tabs.contents.TabInventory;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import dev.xkmc.l2tabs.tabs.inventory.TabRegistry;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = L2Tabs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2TabsClient {

	public static TabToken<InvTabData, TabInventory> TAB_INVENTORY;
	public static TabToken<InvTabData, TabAttributes> TAB_ATTRIBUTE;

	@SubscribeEvent
	public static void client(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			TAB_INVENTORY = TabRegistry.GROUP.registerTab(0, TabInventory::new, () -> Items.CRAFTING_TABLE, L2TabsLangData.INVENTORY.get());
			TAB_ATTRIBUTE = TabRegistry.GROUP.registerTab(1000, TabAttributes::new, () -> Items.IRON_SWORD, L2TabsLangData.ATTRIBUTE.get());

			TabCuriosCompat.onClientInit();
		});
	}

}
