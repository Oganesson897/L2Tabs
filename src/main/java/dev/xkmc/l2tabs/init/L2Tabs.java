package dev.xkmc.l2tabs.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.PacketHandler;
import dev.xkmc.l2tabs.compat.TabCuriosCompat;
import dev.xkmc.l2tabs.tabs.contents.AttributeEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;

@Mod(L2Tabs.MODID)
@Mod.EventBusSubscriber(modid = L2Tabs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2Tabs {

	public static final String MODID = "l2tabs";

	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandler PACKET_HANDLER = new PacketHandler(new ResourceLocation(MODID, "main"), 1,
			e -> e.create(OpenCuriosPacket.class, NetworkDirection.PLAY_TO_CLIENT));

	public L2Tabs() {
		L2TabsConfig.init();
		TabCuriosCompat.onStartup();
		REGISTRATE.addDataGenerator(ProviderType.LANG, L2TabsLangData::genLang);
	}

	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			AttributeEntry.add(() -> Attributes.MAX_HEALTH, false, 1000);
			AttributeEntry.add(() -> Attributes.ARMOR, false, 2000);
			AttributeEntry.add(() -> Attributes.ARMOR_TOUGHNESS, false, 3000);
			AttributeEntry.add(() -> Attributes.KNOCKBACK_RESISTANCE, false, 4000);
			AttributeEntry.add(() -> Attributes.MOVEMENT_SPEED, false, 5000);
			AttributeEntry.add(() -> Attributes.ATTACK_DAMAGE, false, 6000);
			AttributeEntry.add(() -> Attributes.ATTACK_SPEED, false, 7000);
			AttributeEntry.add(ForgeMod.BLOCK_REACH, false, 8000);
			AttributeEntry.add(ForgeMod.ENTITY_REACH, false, 9000);
			AttributeEntry.add(() -> Attributes.LUCK, false, 10000);
			//TODO add 3 attributes
		});
	}

}
