package dev.xkmc.l2tabs.compat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;

public class TabCuriosCompat {

	public static void onStartup() {
		if (ModList.get().isLoaded("curios")) {
			CuriosScreenCompatImpl.get().onStartUp();
		}
	}

	public static void onClientInit() {
		if (ModList.get().isLoaded("curios")) {
			CuriosScreenCompatImpl.get().onClientInit();
		}
	}

	public static void openCuriosTab(ServerPlayer player) {
		if (ModList.get().isLoaded("curios")) {
			CuriosScreenCompatImpl.get().openScreen(player);
		}
	}

}
