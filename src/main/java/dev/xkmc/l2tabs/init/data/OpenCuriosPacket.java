package dev.xkmc.l2tabs.init.data;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

@SerialClass
public record OpenCuriosPacket(OpenCurioHandler event)
		implements SerialPacketBase<OpenCuriosPacket> {

	@Override
	public void handle(@Nullable Player player) {
		if (player instanceof ServerPlayer sp) {
			event.invoke(sp);
		}
	}

}
