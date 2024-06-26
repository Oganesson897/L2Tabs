package dev.xkmc.l2tabs.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2serial.network.PacketHandler;
import dev.xkmc.l2tabs.compat.CuriosEventHandler;
import dev.xkmc.l2tabs.compat.TabCuriosCompat;
import dev.xkmc.l2tabs.init.data.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import top.theillusivec4.curios.api.CuriosApi;

@Mod(L2Tabs.MODID)
@EventBusSubscriber(modid = L2Tabs.MODID, bus = EventBusSubscriber.Bus.MOD)
public class L2Tabs {

	public static final String MODID = "l2tabs";

	public static final Reg REG = new Reg(MODID);
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandler PACKET_HANDLER = new PacketHandler(MODID, 1,
			e -> e.create(OpenCuriosPacket.class, PacketHandler.NetDir.PLAY_TO_SERVER)
	);

	public static final DataMapReg<Attribute, AttrDispEntry> ATTRIBUTE_ENTRY =
			REG.dataMap("attribute_entry", Registries.ATTRIBUTE, AttrDispEntry.class);

	public L2Tabs(IEventBus bus) {
		REG.register(bus);
		L2TabsConfig.init();
		TabCuriosCompat.onStartup();
		REGISTRATE.addDataGenerator(ProviderType.LANG, L2TabsLangData::genLang);
		if (ModList.get().isLoaded(CuriosApi.MODID))
			NeoForge.EVENT_BUS.register(CuriosEventHandler.class);
	}

	@SubscribeEvent
	public static void onPacketReg(RegisterPayloadHandlersEvent event) {
		PACKET_HANDLER.register(event);
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		var gen = event.getGenerator();
		var run = event.includeServer();
		var pack = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		gen.addProvider(run, new AttributeConfigGen(pack, pvd));
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

}
