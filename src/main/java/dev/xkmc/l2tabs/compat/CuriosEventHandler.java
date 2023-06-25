package dev.xkmc.l2tabs.compat;

import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.event.SlotModifiersUpdatedEvent;

@Mod.EventBusSubscriber(modid = L2Tabs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CuriosEventHandler {

	@SubscribeEvent
	public static void onSlotModifierUpdate(SlotModifiersUpdatedEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && player.containerMenu instanceof CuriosListMenu list) {
			ItemStack stack = list.getCarried();
			list.setCarried(ItemStack.EMPTY);
			TabCuriosCompat.openCuriosTab(player);
			player.containerMenu.setCarried(stack);
		}
	}

}
