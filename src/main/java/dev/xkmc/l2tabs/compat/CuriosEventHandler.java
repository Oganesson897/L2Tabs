package dev.xkmc.l2tabs.compat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.SlotModifiersUpdatedEvent;

public class CuriosEventHandler {

	@SubscribeEvent
	public static void onSlotModifierUpdate(SlotModifiersUpdatedEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity.level() instanceof ServerLevel sl) {
			for (var player : sl.players()) {
				if (player.containerMenu instanceof BaseCuriosListMenu<?> menu) {
					if (menu.curios.entity == entity) {
						openMenuWrapped(player, () -> menu.switchPage(player, menu.curios.page));
					}
				}
			}
		}
	}

	public static void openMenuWrapped(ServerPlayer player, Runnable run) {
		var menu = player.containerMenu;
		ItemStack stack = menu.getCarried();
		menu.setCarried(ItemStack.EMPTY);
		run.run();
		menu = player.containerMenu;
		menu.setCarried(stack);
	}

}