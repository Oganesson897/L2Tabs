package dev.xkmc.l2tabs.compat.api;

import dev.xkmc.l2tabs.compat.common.CuriosListMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.fml.ModList;

public class TabCuriosCompat {

	public static void onStartup() {
		if (ModList.get().isLoaded("curios")) {
			AccessoriesMultiplex.onStartUp();
		}
	}

	public static void onClientInit() {
		if (ModList.get().isLoaded("curios")) {
			AccessoriesMultiplex.get().onClientInit();
		}
	}

	public static void openCuriosTab(ServerPlayer player) {
		if (ModList.get().isLoaded("curios")) {
			AccessoriesMultiplex.openScreen(player);
		}
	}

	public static MenuType<CuriosListMenu> getMenuType(){
		return AccessoriesMultiplex.menuType.get();
	}

}
