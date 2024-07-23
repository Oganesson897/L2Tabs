package dev.xkmc.l2tabs.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2serial.network.PacketHandler;
import dev.xkmc.l2tabs.compat.common.CuriosEventHandler;
import dev.xkmc.l2tabs.compat.api.TabCuriosCompat;
import dev.xkmc.l2tabs.init.data.*;
import dev.xkmc.l2tabs.tabs.contents.TabAttributes;
import dev.xkmc.l2tabs.tabs.contents.TabInventory;
import dev.xkmc.l2tabs.tabs.core.TabGroup;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.core.TabType;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.curios.api.CuriosApi;

@Mod(L2Tabs.MODID)
public class L2Tabs {

	public static final String MODID = "l2tabs";

	public static final Reg REG = new Reg(MODID);
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandler PACKET_HANDLER = new PacketHandler(MODID, 1,
			e -> e.create(OpenCuriosPacket.class, PacketHandler.NetDir.PLAY_TO_SERVER)
	);

	public static final DataMapReg<Attribute, AttrDispEntry> ATTRIBUTE_ENTRY =
			REG.dataMap("attribute_entry", Registries.ATTRIBUTE, AttrDispEntry.class);

	public static final L2Registrate.RegistryInstance<TabToken<?, ?>> TABS = REGISTRATE.newRegistry("tab");

	public static final TabGroup<InvTabData> GROUP = new TabGroup<>(TabType.ABOVE);
	public static final SR<TabToken<?, ?>> TAB_REG = SR.of(REG, TABS.reg());
	public static final Val<TabToken<InvTabData, TabInventory>> TAB_INVENTORY =
			TAB_REG.reg("inventory", () -> GROUP.registerTab(0, () -> TabInventory::new,
					() -> Items.CRAFTING_TABLE, L2TabsLangData.INVENTORY.get()));
	public static final Val<TabToken<InvTabData, TabAttributes>> TAB_ATTRIBUTE =
			TAB_REG.reg("attribute", () -> GROUP.registerTab(1000, () -> TabAttributes::new,
					() -> Items.IRON_SWORD, L2TabsLangData.ATTRIBUTE.get()));

	public L2Tabs(IEventBus bus) {
		L2TabsConfig.init();
		TabCuriosCompat.onStartup();
		REGISTRATE.addDataGenerator(ProviderType.LANG, L2TabsLangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, AttributeConfigGen::onDataMapGen);
		if (ModList.get().isLoaded(CuriosApi.MODID))
			NeoForge.EVENT_BUS.register(CuriosEventHandler.class);
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

}
