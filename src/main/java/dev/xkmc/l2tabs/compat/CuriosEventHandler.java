package dev.xkmc.l2tabs.compat;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.SlotModifiersUpdatedEvent;

public class CuriosEventHandler {

	@SubscribeEvent
	public static void onSlotModifierUpdate(SlotModifiersUpdatedEvent event) {
		if (event.getEntity() instanceof Player pl) {
			if (event.getEntity() instanceof ServerPlayer player) {
				if (player.containerMenu instanceof CuriosListMenu list) {
					ItemStack stack = list.getCarried();
					list.setCarried(ItemStack.EMPTY);
					TabCuriosCompat.openCuriosTab(player);
					player.containerMenu.setCarried(stack);
				}
			} else if (event.getEntity().level().isClientSide()) {
				if (pl instanceof LocalPlayer && pl.containerMenu instanceof CuriosListMenu list) {
					list.slots.clear();
				}
			}
		}
	}

}
