package dev.xkmc.l2tabs.init.data;

import dev.xkmc.l2tabs.compat.TabCuriosCompat;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public enum OpenCurioHandler {
	CURIO_OPEN(TabCuriosCompat::openCuriosTab);

	private final Consumer<ServerPlayer> task;

	OpenCurioHandler(Consumer<ServerPlayer> run) {
		this.task = run;
	}

	public void invoke(ServerPlayer player) {
		task.accept(player);
	}

}
