package dev.xkmc.l2tabs.init;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class OpenCuriosPacket extends SerialPacketBase {

	@SerialClass.SerialField
	public OpenCurioHandler event;

	@Deprecated
	public OpenCuriosPacket() {

	}

	public OpenCuriosPacket(OpenCurioHandler event) {
		this.event = event;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		if (context.getSender() != null) {
			event.invoke(context.getSender());
		}
	}
}
