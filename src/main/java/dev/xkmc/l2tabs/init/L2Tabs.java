package dev.xkmc.l2tabs.init;

import dev.xkmc.l2library.init.events.GeneralEventHandler;
import dev.xkmc.l2library.serial.config.ConfigTypeEntry;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2tabs.compat.CuriosEventHandler;
import dev.xkmc.l2tabs.compat.TabCuriosCompat;
import dev.xkmc.l2tabs.init.data.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import top.theillusivec4.curios.api.CuriosApi;

@Mod(L2Tabs.MODID)
@Mod.EventBusSubscriber(modid = L2Tabs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2Tabs {

	public static final String MODID = "l2tabs";

	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandlerWithConfig PACKET_HANDLER = new PacketHandlerWithConfig(MODID, 1,
			e -> e.create(OpenCuriosPacket.class),
			e -> e.create(SyncAttributeToClient.class));

	public static final ConfigTypeEntry<AttributeDisplayConfig> ATTRIBUTE_ENTRY =
			new ConfigTypeEntry<>(PACKET_HANDLER, "attribute_entry", AttributeDisplayConfig.class);

	public L2Tabs() {
		L2TabsConfig.init();
		TabCuriosCompat.onStartup();
		REGISTRATE.addDataGenerator(ProviderType.LANG, L2TabsLangData::genLang);
		if (ModList.get().isLoaded(CuriosApi.MODID))
			NeoForge.EVENT_BUS.register(CuriosEventHandler.class);
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		event.getGenerator().addProvider(event.includeServer(), new AttributeConfigGen(event.getGenerator()));
	}

	public static void onAttributeUpdate(LivingEntity le) {
		var f = SyncAttributeToClient.of(le);
		if (f.list().isEmpty()) return;
		if (le instanceof ServerPlayer sp) {
			if (L2TabsConfig.SERVER.syncPlayerAttributeName.get()) {
				GeneralEventHandler.schedule(() -> PACKET_HANDLER.toClientPlayer(f, sp));
			}
		}
		if (L2TabsConfig.SERVER.syncAllEntityAttributeName.get()) {
			GeneralEventHandler.schedule(() -> PACKET_HANDLER.toTrackingPlayers(f, le));
		}
	}

}
