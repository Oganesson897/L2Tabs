package dev.xkmc.l2tabs.compat.track;

import dev.xkmc.l2menustacker.screen.base.LayerPopType;
import dev.xkmc.l2menustacker.screen.track.TrackedEntryType;
import dev.xkmc.l2tabs.compat.api.AccessoriesMultiplex;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CurioInvTrace extends TrackedEntryType<CurioTraceData> {

	@Override
	public LayerPopType restoreMenuNotifyClient(ServerPlayer player, CurioTraceData data, @Nullable Component comp) {
		AccessoriesMultiplex.get().openCuriosInv(player, data);
		return LayerPopType.CLEAR;
	}

}
