package dev.xkmc.l2tabs.compat.api;

import dev.xkmc.l2tabs.compat.track.CurioTraceData;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

public class TabCuriosCompat {

	public static void onStartup() {
		if (ModList.get().isLoaded("curios")) {
			AccessoriesMultiplex.onStartUp();
		}
	}

	public static void onCommonSetup() {
		if (ModList.get().isLoaded("curios")) {
			AccessoriesMultiplex.onCommonSetup();
		}
	}

	public static void onClientInit() {
		if (ModList.get().isLoaded("curios")) {
			AccessoriesMultiplex.get().onClientInit();
		}
	}

	public static void openCuriosTab(ServerPlayer player) {
		if (ModList.get().isLoaded("curios")) {
			AccessoriesMultiplex.openScreen(player, new CurioTraceData(0));
		}
	}

}
