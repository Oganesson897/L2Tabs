package dev.xkmc.l2tabs.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.ConfigTypeEntry;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2tabs.compat.TabCuriosCompat;
import dev.xkmc.l2tabs.init.data.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;

@Mod(L2Tabs.MODID)
@Mod.EventBusSubscriber(modid = L2Tabs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2Tabs {

	public static final String MODID = "l2tabs";

	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandlerWithConfig PACKET_HANDLER = new PacketHandlerWithConfig(new ResourceLocation(MODID, "main"), 1,
			"l2tabs_config",
			e -> e.create(OpenCuriosPacket.class, NetworkDirection.PLAY_TO_SERVER));

	public static final ConfigTypeEntry<AttributeDisplayConfig> ATTRIBUTE_ENTRY =
			new ConfigTypeEntry<>(PACKET_HANDLER, "attribute_entry", AttributeDisplayConfig.class);

	public L2Tabs() {
		L2TabsConfig.init();
		TabCuriosCompat.onStartup();
		REGISTRATE.addDataGenerator(ProviderType.LANG, L2TabsLangData::genLang);
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		event.getGenerator().addProvider(event.includeServer(), new AttributeConfigGen(event.getGenerator()));
	}

}
