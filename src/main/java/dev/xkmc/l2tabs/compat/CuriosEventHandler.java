package dev.xkmc.l2tabs.compat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import top.theillusivec4.curios.api.event.SlotModifiersUpdatedEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class CuriosEventHandler {

	@SubscribeEvent
	public static void onSlotModifierUpdate(SlotModifiersUpdatedEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity.level() instanceof ServerLevel sl) {
			for (var player : sl.players()) {
				if (player.containerMenu instanceof BaseCuriosListMenu<?> menu) {
					if (menu.curios.entity == entity) {
						MAP.put(player, () -> openMenuWrapped(player,
								() -> menu.switchPage(player, menu.curios.page)));
					}
				}
			}
		}
	}

	private static final Map<Player, Runnable> MAP = new LinkedHashMap<>();

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onPlayerTick(EntityTickEvent event) {
		if (!MAP.isEmpty() && event.getEntity() instanceof ServerPlayer player) {
			var run = MAP.get(player);
			if (run != null) {
				if (player.isAlive() && !player.hasDisconnected()) {
					run.run();
				}
				MAP.remove(player);
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