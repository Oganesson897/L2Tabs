package dev.xkmc.l2tabs.init;

import dev.xkmc.l2tabs.compat.TabCuriosCompat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = L2Tabs.MODID, bus = EventBusSubscriber.Bus.MOD)
public class L2TabsClient {

	@SubscribeEvent
	public static void client(FMLClientSetupEvent event) {
		TabCuriosCompat.onClientInit();
	}

}
