package dev.xkmc.l2tabs.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import dev.xkmc.l2library.serial.config.ConfigTypeEntry;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2tabs.compat.CuriosEventHandler;
import dev.xkmc.l2tabs.compat.TabCuriosCompat;
import dev.xkmc.l2tabs.init.data.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import top.theillusivec4.curios.api.CuriosApi;

@Mod(L2Tabs.MODID)
@Mod.EventBusSubscriber(modid = L2Tabs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2Tabs {

	public static final String MODID = "l2tabs";

	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandlerWithConfig PACKET_HANDLER = new PacketHandlerWithConfig(new ResourceLocation(MODID, "main"), 1,
			e -> e.create(OpenCuriosPacket.class, NetworkDirection.PLAY_TO_SERVER),
			e -> e.create(SyncAttributeToClient.class, NetworkDirection.PLAY_TO_CLIENT));

	public static final ConfigTypeEntry<AttributeDisplayConfig> ATTRIBUTE_ENTRY =
			new ConfigTypeEntry<>(PACKET_HANDLER, "attribute_entry", AttributeDisplayConfig.class);

	public L2Tabs() {
		L2TabsConfig.init();
		TabCuriosCompat.onStartup();
		REGISTRATE.addDataGenerator(ProviderType.LANG, L2TabsLangData::genLang);
		if (ModList.get().isLoaded(CuriosApi.MODID))
			MinecraftForge.EVENT_BUS.register(CuriosEventHandler.class);
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		event.getGenerator().addProvider(event.includeServer(), new AttributeConfigGen(event.getGenerator()));
	}

	public static void onAttributeUpdate(LivingEntity le) {
		var f = new SyncAttributeToClient(le);
		if (f.list.isEmpty()) return;
		if (le instanceof ServerPlayer sp) {
			if (L2TabsConfig.COMMON.syncPlayerAttributeName.get()) {
				GeneralEventHandler.schedule(() -> PACKET_HANDLER.toClientPlayer(f, sp));
			}
		}
		if (L2TabsConfig.COMMON.syncAllEntityAttributeName.get()) {
			GeneralEventHandler.schedule(() -> PACKET_HANDLER.toTrackingPlayers(f, le));
		}
	}

}
